package org.drama.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drama.annotation.ElementProperty;
import org.drama.annotation.LayerDescription;
import org.drama.annotation.LayerProperty;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.DramaException;
import org.drama.vo.BiParameterValueObject;
import org.drama.vo.KeyValueObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.drama.delegate.Delegator.*;
/**
 * Stage 和 layer 的运转内核
 */
class DramaKernel implements Kernel {
    private static final Map<Signature, DramaKernel> INSTANCE_MAP;

    static {
        INSTANCE_MAP = new ConcurrentHashMap<>();
    }

    private final Set<Class<? extends Event>> registeredEventSet;
    private final Map<KeyValueObject<Class<? extends Event>, LayerContainer>, Set<ElementContainer>> handingMap;
    private final Set<LayerContainer> layerContainerSet;
    private final Map<Layer, Set<Element>> elementMap;
    private Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> layerGenerator;

    private DramaKernel() {
        registeredEventSet = new HashSet<>();
        handingMap = new ConcurrentHashMap<>();
        elementMap = new ConcurrentHashMap<>();
        layerContainerSet = new TreeSet<>();
    }

    public static Kernel getInstance(Signature signature) {
        return INSTANCE_MAP.computeIfAbsent(Objects.requireNonNull(signature), s -> new DramaKernel());
    }

    @Override
    public void setLayerGenerator(
            Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> generator) {

        layerGenerator = generator;
    }

    @Override
    public Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> getLayerGenerator() {
        return layerGenerator;
    }

    @Override
    public boolean reset() {
        registeredEventSet.clear();
        handingMap.clear();
        layerContainerSet.clear();
        return true;
    }

    private Set<ElementContainer> getElemSet(Class<? extends Event> clazz, LayerContainer LayerContainer) {
        KeyValueObject<Class<? extends Event>, LayerContainer> idx = new KeyValueObject<>(clazz, LayerContainer);
        return handingMap.computeIfAbsent(idx, i -> new TreeSet<>());
    }

    private LayerContainer getLayerContainer(Class<? extends Layer> layerClazz) {
        LayerProperty prop = layerClazz.getAnnotation(LayerProperty.class);

        if (Objects.isNull(prop)) {
            throw DramaException.noSpecialLayerProp(layerClazz);
        }

        LayerDescriptor layerDesc = getLayerDescriptor(prop.uuid(), prop.name(), prop.priority(), prop.disabled(), prop.excludeEvent());

        return getLayerContainer(layerClazz, layerDesc);
    }

    private LayerContainer getLayerContainer(final Class<? extends Layer> clz, LayerDescriptor desc) {
        final UUID identity = UUID.fromString(desc.getUUID());

        Optional<LayerContainer> opt = layerContainerSet.stream().filter(l -> Objects.equals(l.getIdentity(), identity)).findFirst();

        if(opt.isPresent()) {
            return opt.get();
        }

        Layer layer = func(layerGenerator, new BiParameterValueObject<>(clz, desc));

        if (Objects.isNull(layer)) {
            throw DramaException.illegalLayerDesc(desc);
        }

        LayerContainer layerContainer = new LayerContainer(layer, identity, desc.getName(), desc.getPriority());
        layerContainer.setDisable(desc.getDisabled());
        layerContainer.setExcludeEvent(desc.getExculdeEvent());
        return layerContainer;
    }

    private LayerContainer getLayerContainer(ElementProperty elemProp) {
        LayerContainer layerContainer;

        Class<? extends Layer> layerClazz = elemProp.layer();

        // 如果指定的 layer 有描述注解，则按描述注解进行查找和分配
        if (!Objects.equals(layerClazz, Layer.Null.class)) {
            layerContainer = getLayerContainer(layerClazz);
        } else {
            // 根据指定的 layer 没有找到，则通过 ElementProperty 提供给的 layerDesc 进行分配
            LayerDescription layerDesc = Objects.requireNonNull(elemProp.layerDesc());

            final String enumTargetName = layerDesc.target();

            Optional<LayerDescriptor> optDesc =
                    Arrays.stream(layerDesc.desc().getEnumConstants())
                            .map(e -> (LayerDescriptor) e)
                            .filter(e -> Objects.equals(e.getName(), enumTargetName))
                            .findFirst();

            if (optDesc.isPresent()) {
                layerContainer = getLayerContainer(layerClazz, optDesc.get());
            } else {
                throw DramaException.illegalLayerDesc(layerDesc);
            }
        }

        if (Objects.nonNull(layerContainer)) {
            layerContainerSet.add(layerContainer);
        }

        return layerContainer;
    }

    private LayerDescriptor getLayerDescriptor(
            String uuid, String name, int priority, boolean disabled, Class<? extends Event>[] event) {

        return new LayerDescriptor() {
            @Override
            public boolean getDisabled() {
                return disabled;
            }

            @Override
            public Class<? extends Event>[] getExculdeEvent() {
                return event;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getPriority() {
                return priority;
            }

            @Override
            public String getUUID() {
                return uuid;
            }
        };
    }

    @Override
    public final ImmutableSet<Layer> getlayers() {
        Set<Layer> layers = new LinkedHashSet<>();

        layerContainerSet.forEach(l -> layers.add(l.getLayer()));

        return ImmutableSet.newInstance(layers);
    }

    @Override
    public ImmutableSet<Element> getElements(Layer layer) {
        return ImmutableSet.newInstance(elementMap.getOrDefault(layer, new HashSet<>()));
    }

    @Override
    public void notifyHandler(final Layer layer, final Event event, final Consumer<LayerContainer> onPreHanding,
                              final Consumer<ElementContainer> onCompleted) {

        Class<? extends Event> eventClass = event.getClass();

        Optional<LayerContainer> opt = layerContainerSet.stream().filter(l -> Objects.equals(l.getLayer(), layer)).findFirst();

        if (!opt.isPresent()) {
            return;
        }

        LayerContainer layerCon = opt.get();

        if (layerCon.getDisabled()) {
            return;
        }

        if (Objects.nonNull(layerCon.getExcludeEvent()) && ArrayUtils.contains(layerCon.getExcludeEvent(), eventClass)) {
            return;
        }

        KeyValueObject<Class<? extends Event>, LayerContainer> key = new KeyValueObject<>(eventClass, layerCon);
        Set<ElementContainer> elemSet = handingMap.get(key);

        if (CollectionUtils.isEmpty(elemSet)) {
            return;
        }

        action(onPreHanding, key.getValue());

        forEach(elemSet, (elemCon, index) -> {
            elemCon.setCurrentLayer(layer);

            Element elem = elemCon.getInvocator();

            elem.handing(event);

            action(onCompleted, elemCon);

            elemCon.setCurrentLayer(null);

            return !Objects.equals(elem.getBroadcastStatus(), BroadcastStatus.Transmit);
        });
    }

    @Override
    public boolean regeisterEvent(Set<Class<? extends Event>> classSet) {
        if (CollectionUtils.isEmpty(classSet)) {
            return false;
        }

        classSet.forEach((eClz) -> {
            if (!registeredEventSet.contains(eClz) && !eClz.equals(Event.class)) {
                registeredEventSet.add(eClz);
            }
        });
        return true;
    }

    @Override
    public Layer registerElement(Element element) {
        if (CollectionUtils.isEmpty(registeredEventSet)) {
            throw DramaException.emptyRegisterEvents();
        }

        ElementProperty prop = Objects.requireNonNull(element.getClass().getAnnotation(ElementProperty.class));
        LayerContainer LayerContainer = Objects.requireNonNull(getLayerContainer(prop));
        Class<? extends Event>[] events = Objects.requireNonNull(prop.events());

        // 注册全局元素
        if (ArrayUtils.contains(events, Event.class)) {
            if (events.length != 1) {
                throw DramaException.onlyGlobaleEvent(element.getClass());
            }
            registeredEventSet.forEach(clazz -> {
                Set<ElementContainer> elemSet = getElemSet(clazz, LayerContainer);
                bindElementHandler(element, prop, elemSet);
            });
        } else {
            // 注册非全局元素
            for (Class<? extends Event> clazz : events) {
                registeredEventSet.forEach(registeredEvent -> {
                    if (Objects.equals(clazz, registeredEvent)) {
                        Set<ElementContainer> elemSet = getElemSet(clazz, LayerContainer);
                        bindElementHandler(element, prop, elemSet);
                    }
                });
            }
        }

        Layer layer = LayerContainer.getLayer();
        Set<Element> elementSet = elementMap.computeIfAbsent(layer, l -> new HashSet<>());
        elementSet.add(element);
        return layer;
    }

    private void bindElementHandler(Element element, ElementProperty prop, Set<ElementContainer> elemSet) {
        ElementContainer elemCon = new ElementContainer(element);
        elemCon.setPriority(prop.priority());
        elemSet.add(elemCon);
    }
}

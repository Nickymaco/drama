package org.drama.core;

import org.drama.annotation.ElementProperty;
import org.drama.annotation.LayerDescription;
import org.drama.annotation.LayerProperty;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.DramaException;
import org.drama.security.Signature;
import org.drama.vo.BiParameterValueObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.drama.delegate.Delegator.func;

/**
 * Stage 和 layer 的运转内核
 */
class DramaKernel implements Kernel {
    private final static Map<Signature, DramaKernel> INSTANCE_MAP = new ConcurrentHashMap<>();
    private final Set<LayerContainer> layerContainerSet = new TreeSet<>();
    private Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> layerGenerator;

    static Kernel getInstance(Signature signature) {
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
        layerContainerSet.clear();
        return true;
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

        if (opt.isPresent()) {
            return opt.get();
        }

        Layer layer = func(layerGenerator, new BiParameterValueObject<>(clz, desc));

        if (Objects.isNull(layer)) {
            throw DramaException.illegalLayerDesc(desc);
        }

        LayerContainer layerContainer = new LayerContainer(layer, identity, desc.getName(), desc.getPriority(), desc.getExculdeEvent());
        layerContainer.setDisable(desc.getDisabled());
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
            String uuid, String name, int priority, boolean disabled, String[] event) {

        return new LayerDescriptor() {
            @Override
            public boolean getDisabled() {
                return disabled;
            }

            @Override
            public String[] getExculdeEvent() {
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
        Optional<LayerContainer> opt = layerContainerSet.stream().filter(l -> Objects.equals(l.getLayer(), layer)).findFirst();
        return opt.map(LayerContainer::getElements).orElse(null);
    }

    @Override
    public void notifyHandler(final Layer layer, final Event event,
                              final Consumer<LayerContainer> onPreHanding, final Consumer<ElementContainer> onCompleted) {

        Optional<LayerContainer> opt = layerContainerSet.stream().filter(l -> Objects.equals(l.getLayer(), layer)).findFirst();

        if (!opt.isPresent()) {
            return;
        }

        opt.get().handingEevnt(event, onPreHanding, onCompleted);
    }


    @Override
    public Layer registerElement(Element element) {
        ElementContainer elemCon = new ElementContainer(element);

        LayerContainer layerContainer = getLayerContainer(elemCon.getElementProperty());
        layerContainer.addElement(elemCon);

        return layerContainer.getLayer();
    }
}

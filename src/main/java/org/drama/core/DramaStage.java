package org.drama.core;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.forEach;
import static org.joor.Reflect.on;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.drama.annotation.EventProperty;
import org.drama.collections.ImmutableSet;
import org.drama.core.LayerContants.NullLayer;
import org.drama.event.Event;
import org.drama.event.EventResultEntity;
import org.drama.exception.DramaException;
import org.drama.log.template.StageLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
public class DramaStage implements Stage {
    private StageLoggingTemplate logging;
    private ImmutableSet<Layer> layers;
    private Configuration configuration;
    private Kernel kernel;
    private Map<Integer, Class<? extends Event>> registeredEvent = new HashMap<>();

    @Override
    public ImmutableSet<Layer> getLayers() {
        if (CollectionUtils.isEmpty(layers)) {
            layers = kernel.getlayers();
        }
        return layers;
    }

    @Override
    public Render play(Event[] events) throws DramaException {
        return play(events, null, null);
    }

    @Override
    public Render play(Event[] events, PlayListener playListener, BroadcastListener bLisenter) throws DramaException {
        DramaRender render = new DramaRender();
        play(render, events, playListener, bLisenter);
        return render;
    }

    @Override
    public void play(Render render, Event[] events, PlayListener playListener, BroadcastListener bLisenter) throws DramaException {
        validation(events);

        getLogging().recevieEvent(events);

        // set play listener
        final PlayListener playLisenter = ObjectUtils.defaultIfNull(playListener, event -> {});
        // set broadcast listener
        final BroadcastListener broadcastlisenter = ObjectUtils.defaultIfNull(bLisenter, new BroadcastListener() {
            private BroadcastStatus broadcastStatus;

            @Override
            public BroadcastStatus getBroadcastStatus() {
                return broadcastStatus;
            }

            @Override
            public void setBroadcastStatus(BroadcastStatus broadcastStatus) {
                this.broadcastStatus = broadcastStatus;
            }
        });

        broadcastlisenter.setBroadcastStatus(BroadcastStatus.Transmit);
        
        final Map<String, Object> modelMap = new HashMap<>();
        
        forEach(events, p -> {
            try(Event e = p.getParam1()){
                if (Objects.equals(broadcastlisenter.getBroadcastStatus(), BroadcastStatus.Exit)) {
                    return;
                }

                if (playLisenter.onBeforePlay(e)) {
                    return;
                }

                play(e, modelMap, broadcastlisenter);

                playLisenter.onCompletedPlay(e);
            } catch (Exception ex) {
               throw DramaException.occurredPlayError(ex, p.getParam1());
            }
        });

        render.setCode(Render.SUCCESS);
        render.setModel(modelMap);
    }

    /**
     * 根据监听器判断是否继续往下执行
     */
    private void play(Event event, final Map<String, Object> modelMap, BroadcastListener lisenter) {
        if (Objects.isNull(event) || CollectionUtils.isEmpty(getLayers())) {
            return;
        }
        
        getLogging().dealEvent(event);
        // layer execute broadcast
        forEach(getLayers(), (layer, i) -> {
        	layer.broadcast(event, lisenter);
        	return Objects.equals(lisenter.getBroadcastStatus(), BroadcastStatus.Exit);
        });
        // collect results
        event.getEventResult().allResults().stream().filter(r -> r.getOutput()).forEach(r -> {
            EventResultEntity entity = r.getValue();
            String aliasName = entity.aliasName();
            modelMap.put(StringUtils.isBlank(aliasName) ? entity.getClass().getSimpleName() : aliasName, entity);
        });
    }
    
    private void validation(Event[] events) {
        if (ArrayUtils.isEmpty(events)) {
            throw DramaException.emptyRegisterEvents();
        }

        forEach(events, p -> {
            Event e = p.getParam1();

            if(StringUtils.isBlank(e.getName())) {
                throw DramaException.illegalRegisterEvent(e);
            }

            Class<?> eClazz = e.getClass();

            if(Objects.equals(eClazz, DramaEvent.class)) {
                return;
            }

            if(Objects.isNull(registeredEvent.get(eClazz.getSimpleName().hashCode()))) {
                Class<?> aClass = registeredEvent.get(e.getName().hashCode());

                if(ObjectUtils.notEqual(aClass, eClazz)) {
                    throw DramaException.illegalRegisterEvent(e);
                }
            }
        });
    }

    @Override
    public void setup(Configuration configuration) throws DramaException {
        this.configuration = Objects.requireNonNull(configuration);

        kernel = KernelFactory.getInstance().getKernel(configuration.getSignature());

        // 清空逻辑处理层，以便可以重新获取
        layers = null;

        logging = LoggingTemplateFactory.getStageLoggingTemplate(configuration.getLoggingFactory());
        logging.setup(this);

        kernel.reset();

        if (ArrayUtils.isNotEmpty(configuration.regeisterEventPackage())) {
            registerEvent(configuration);
        }

        // 获取注册元素工厂
        final RegisterElementFactory registerElementFactory = configuration.getRegisterElementFactory();

        if (Objects.isNull(registerElementFactory)) {
            throw DramaException.emptyRegisterElements();
        }
        // 自定义逻辑处理层构建工厂
        final LayerFactory layerFacotry = configuration.getLayerFactory();

        // 注册逻辑处理层
        final Set<LayerDescriptor> layerDescList = new HashSet<>();

        if (Objects.isNull(kernel.getLayerGenerator())) {
            addLayerGenerator(layerFacotry, layerDescList::add);
        }

        if (!registerElement(registerElementFactory.elements())) {
            throw DramaException.emptyRegisterElements();
        }
        // 打印注册了哪些逻辑处理层
        layerDescList.forEach(desc -> getLogging().regeisteredLayer(new String[]{desc.getName()}));
    }

    @SuppressWarnings("unchecked")
    private void registerEvent(Configuration configuration) {
        final DramaClassloader classloader = new DramaClassloader();

        registeredEvent.clear();

        forEach(configuration.regeisterEventPackage(), p -> classloader.scan(p.getParam1(), c -> {
            if (!c.equals(Event.class) && !c.equals(DramaEvent.class) && ClassUtils.getAllInterfaces(c).contains(Event.class)) {
                Class<? extends Event> c1 = (Class<? extends Event>) c;
                EventProperty eventProperty = c1.getAnnotation(EventProperty.class);

                if(Objects.nonNull(eventProperty) && ArrayUtils.isNotEmpty(eventProperty.aliasFor())) {
                    forEach(eventProperty.aliasFor(), p1 -> registeredEvent.put(p1.getParam1().hashCode(), c1));
                }
                registeredEvent.put(c.getSimpleName().hashCode(), c1);
            }
        }));
    }

    @Override
    public PlayWizard wizard() {
        return new DramaPlayWizard(this, this::getEventClass);
    }

    private void addLayerGenerator(final LayerFactory layerFacotry, final Consumer<LayerDescriptor> onCreated) {
        kernel.setLayerGenerator(p -> {
            Layer layer;

            if (Objects.equals(NullLayer.class, p.getParam1())) {
                layer = on(DramaLayer.class).create().get();
            } else if (Objects.isNull(layerFacotry)) {
                layer = on(p.getParam1()).create().get();
            } else {
                layer = layerFacotry.getLayer(p.getParam1());

                if (Objects.isNull(layer)) {
                    layer = layerFacotry.getLayer(p.getParam2());
                }
            }

            if (Objects.nonNull(layer)) {
                layer.setConfiguration(configuration);

                action(onCreated, p.getParam2());
            }
            return layer;
        });
    }

    private boolean registerElement(Set<Element> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }

        for (Element element : elements) {
            Layer layer = kernel.registerElement(element);

            if (Objects.isNull(layer)) {
                return false;
            }

            getLogging().regeisteredElement(new Class<?>[]{element.getClass()});
        }
        return true;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    protected StageLoggingTemplate getLogging() {
        return logging;
    }

    private Class<? extends Event> getEventClass(String name) {
        return registeredEvent.get(name.hashCode());
    }
}

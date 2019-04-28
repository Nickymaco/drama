package org.drama.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.InheritanceUtils;
import org.drama.annotation.DramaAlias;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.event.EventResult;
import org.drama.event.EventResultEntity;
import org.drama.event.EventResultValue;
import org.drama.exception.DramaException;
import org.drama.log.template.IStageLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

import java.util.*;
import java.util.function.Consumer;

import static org.drama.delegate.Delegator.action;
import static org.joor.Reflect.on;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
public class DramaStage implements Stage {
    private final ThreadLocal<Render> renderThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<BroadcastLisenter> broadcastLisenterThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<PlayLisenter> playLisenterThreadLocal = new ThreadLocal<>();
    private IStageLoggingTemplate logging;
    private ImmutableSet<Layer> layers;
    private Configuration configuration;
    private Kernel kernel;

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
    public Render play(Event[] events, PlayLisenter pLisenter, BroadcastLisenter bLisenter) throws DramaException {
        play(null, events, pLisenter, bLisenter);

        return getRender();
    }

    @Override
    public void play(Render render, Event[] events, PlayLisenter pLisenter, BroadcastLisenter bLisenter) throws DramaException {
        if (ArrayUtils.isEmpty(events)) {
            throw DramaException.emptyRegisterEvents();
        }

        setRender(render);
        setPlayLisenter(pLisenter);
        setBroadcastLisenter(bLisenter);

        getLogging().recevieEvent(events);

        final PlayLisenter playLisenter = getPlayLisenter();
        final BroadcastLisenter broadcastlisenter = getBroadcasetLisenter();
        final Map<String, Object> modelMap = new HashMap<>();

        for (Event event : events) {
            if (playLisenter.onBeforePlay(event)) {
                break;
            }

            broadcastlisenter.setBroadcastStatus(BroadcastStatus.Transmit);

            Class<?> eventClazz = event.getClass();
            // 检查注册事件类型范围
            if (!eventClazz.equals(Event.class) && InheritanceUtils.distance(eventClazz, DramaEvent.class) == 0) {
                throw DramaException.illegalRegisterEvent(eventClazz);
            }

            playDeal(event, modelMap, broadcastlisenter);

            playLisenter.onCompletedPlay(event);

            if (Objects.equals(broadcastlisenter.getBroadcastStatus(), BroadcastStatus.Exit)) {
                break;
            }
        }

        getRender().setCode(Render.SUCCESS);
        getRender().setModel(modelMap);
    }

    protected void playDeal(Event event, final Map<String, Object> modelMap, BroadcastLisenter lisenter) {
        getLogging().dealEvent(event);

        if (!(event instanceof DramaEvent)) {
            throw DramaException.illegalRegisterEvent(event.getClass());
        }

        DramaEvent<?> dramaEvent = (DramaEvent<?>) event;
        dramaEvent.setEventResult(new EventResult(dramaEvent));

        playDealEvent(dramaEvent, lisenter);

        EventResult eventResult = dramaEvent.getEventResult();
        Collection<EventResultValue> resultValues = eventResult.allResults();

        resultValues.forEach(r -> {
            if (!r.getOutput()) {
                return;
            }

            Class<?> clzR = r.getValue().getClass();
            DramaAlias aliasName = clzR.getAnnotation(DramaAlias.class);

            if (Objects.nonNull(aliasName) && StringUtils.isNotBlank(aliasName.value())) {
                modelMap.put(aliasName.value(), r.getValue());
            } else {
                EventResultEntity entity = r.getValue();
                Class<?> clzEntity = entity.getClass();
                String simpleName = clzEntity.getSimpleName();
                modelMap.put(simpleName, entity);
            }
        });
    }

    /**
     * 根据监听器判断是否继续往下执行
     */
    protected void playDealEvent(Event event, BroadcastLisenter lisenter) {
        if (Objects.isNull(event) || CollectionUtils.isEmpty(getLayers())) {
            return;
        }

        for (Layer layer : getLayers()) {
            try {
                layer.broadcast(event, lisenter);
            } catch (Throwable e) {
                throw DramaException.occurredPlayError(e, event);
            }

            if (Objects.equals(lisenter.getBroadcastStatus(), BroadcastStatus.Exit)) {
                break;
            }
        }
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

        // 获取注册事件工厂
        final RegisterEventFactory registerEventFactory = configuration.getRegisterEventFactory();

        if (Objects.isNull(registerEventFactory)) {
            throw DramaException.emptyRegisterEvents();
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
            addLayerGenerator(layerFacotry, l -> layerDescList.add(l));
        }

        if (!registerEvent(registerEventFactory.events())) {
            throw DramaException.errorRegisterEvents();
        }

        if (!registerElement(registerElementFactory.elements())) {
            throw DramaException.emptyRegisterElements();
        }
        // 打印注册了哪些逻辑处理层
        layerDescList.forEach(desc -> getLogging().regeisteredLayer(new String[]{desc.getName()}));
    }

    protected void addLayerGenerator(final LayerFactory layerFacotry, final Consumer<LayerDescriptor> onCreated) {
        kernel.setLayerGenerator((p) -> {
            Layer layer;

            if (Objects.equals(Layer.Null.class, p.getParam1())) {
                layer = on(DramaLayer.class).create().get();
            } else if (Objects.isNull(layerFacotry)) {
                return on(p.getParam1()).create().get();
            } else {
                layer = layerFacotry.getLayer(p.getParam1());

                if (Objects.isNull(layer)) {
                    layer = layerFacotry.getLayer(p.getParam2());
                }
            }

            if (Objects.nonNull(layer)) {
                layer.setConfiguration(configuration);
            }

            if(Objects.nonNull(layer)) {
                action(onCreated, p.getParam2());
            }
            return layer;
        });
    }

    protected boolean registerElement(Set<Element> elements) {
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

    protected boolean registerEvent(Set<Class<? extends Event>> events) {
        if (CollectionUtils.isEmpty(events)) {
            return false;
        }

        kernel.regeisterEvent(events);

        getLogging().registeredEvent(events.toArray(new Class<?>[]{}));

        return true;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    protected PlayLisenter getPlayLisenter() {
        return playLisenterThreadLocal.get();
    }

    protected void setPlayLisenter(PlayLisenter lisenter) {
        playLisenterThreadLocal.set(ObjectUtils.defaultIfNull(lisenter, PlayLisenter.NULL));
    }

    protected BroadcastLisenter getBroadcasetLisenter() {
        return broadcastLisenterThreadLocal.get();
    }

    protected void setBroadcastLisenter(BroadcastLisenter lisenter) {
        broadcastLisenterThreadLocal.set(ObjectUtils.defaultIfNull(lisenter, BroadcastLisenter.Default));
    }

    protected Render getRender() {
        return renderThreadLocal.get();
    }

    protected void setRender(Render render) {
        renderThreadLocal.set(ObjectUtils.defaultIfNull(render, new DramaRender()));
    }

    protected IStageLoggingTemplate getLogging() {
        return logging;
    }
}

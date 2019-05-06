package org.drama.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.event.EventResult;
import org.drama.event.EventResultEntity;
import org.drama.exception.DramaException;
import org.drama.log.template.IStageLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

import java.util.*;
import java.util.function.Consumer;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.forEach;
import static org.joor.Reflect.on;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
public class DramaStage implements Stage {
    private IStageLoggingTemplate logging;
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
    public Render play(Event[] events, PlayLisenter playListener, BroadcastListener bLisenter) throws DramaException {
        DramaRender render = new DramaRender();
        play(render, events, playListener, bLisenter);
        return render;
    }

    @Override
    public void play(Render render, Event[] events, PlayLisenter playListener, BroadcastListener bLisenter) throws DramaException {
        eventValidation(events);

        getLogging().recevieEvent(events);

        final PlayLisenter playLisenter = ObjectUtils.defaultIfNull(playListener, PlayLisenter.NULL);
        final BroadcastListener broadcastlisenter = ObjectUtils.defaultIfNull(bLisenter, BroadcastListener.Default);
        final Map<String, Object> modelMap = new HashMap<>();

        for (Event event : events) {
            if (playLisenter.onBeforePlay(event)) {
                break;
            }

            broadcastlisenter.setBroadcastStatus(BroadcastStatus.Transmit);

            playDeal(event, modelMap, broadcastlisenter);

            playLisenter.onCompletedPlay(event);

            if (Objects.equals(broadcastlisenter.getBroadcastStatus(), BroadcastStatus.Exit)) {
                break;
            }
        }

        render.setCode(Render.SUCCESS);
        render.setModel(modelMap);
    }

    private void eventValidation(Event[] events) {
        if (ArrayUtils.isEmpty(events)) {
            throw DramaException.emptyRegisterEvents();
        }

        forEach(events, p -> {
            Class<? extends Event> clazz = p.getParam1().getClass();

            if(Objects.equals(clazz, DramaEvent.class)) {
                return;
            }

            if(Objects.isNull(registeredEvent.get(clazz.getSimpleName().hashCode()))) {
                throw DramaException.illegalRegisterEvent(clazz);
            }
        });
    }

    private void playDeal(Event event, final Map<String, Object> modelMap, BroadcastListener lisenter) {
        getLogging().dealEvent(event);

        playDealEvent(event, lisenter);

        EventResult result = Objects.requireNonNull(event.getEventResult());

        result.allResults().forEach(r -> {
            if (!r.getOutput()) {
                return;
            }

            EventResultEntity entity = r.getValue();

            if (StringUtils.isNotBlank(entity.aliasName())) {
                modelMap.put(entity.aliasName(), entity);
            } else {
                modelMap.put(entity.getClass().getSimpleName(), entity);
            }
        });
    }

    /**
     * 根据监听器判断是否继续往下执行
     */
    private void playDealEvent(Event event, BroadcastListener lisenter) {
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
            if (ObjectUtils.notEqual(c, Event.class) && ClassUtils.getAllInterfaces(c).contains(Event.class)) {
                registeredEvent.put(c.getSimpleName().hashCode(), (Class<? extends Event>) c);
            }
        }));
    }

    @Override
    public PlayWizard wizard() {
        return new DramaPlayWizard(this, (name) -> registeredEvent.get(name.hashCode()));
    }

    private void addLayerGenerator(final LayerFactory layerFacotry, final Consumer<LayerDescriptor> onCreated) {
        kernel.setLayerGenerator((p) -> {
            Layer layer;

            if (Objects.equals(Layer.Null.class, p.getParam1())) {
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

    protected IStageLoggingTemplate getLogging() {
        return logging;
    }
}

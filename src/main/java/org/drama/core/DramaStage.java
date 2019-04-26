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

import static org.joor.Reflect.on;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
public class DramaStage implements Stage {
	protected static final String DEFAULT_LAYER_CLASS = "org.drama.core.DramaLayer";
	private final ThreadLocal<StageRender> currentRender = new ThreadLocal<>();
	private IStageLoggingTemplate logging;
	private ImmutableSet<Layer> layers;
	private Configuration configuration;
	private Kernel kernel;
	
	@Override
	public ImmutableSet<Layer> getLayers() {
		if(Objects.isNull(layers)) {
			layers = kernel.getlayers();
		}
		return layers;
	}
	
	protected IStageLoggingTemplate getLogging() {
		return logging;
	}
	
	@Override
	public Render play(PlayLisenter lisenter, Event... events) throws DramaException {
		currentRender.set(new StageRender());

		if (ArrayUtils.isEmpty(events)) {
			currentRender.get().setCode(Render.FAILURE);
			currentRender.get().setMessage(Render.UNFOUND_EVENT_MSG);
			currentRender.get().setModel(null);
			return currentRender.get();
		}

		getLogging().recevieEvent(events);

		Map<String, Object> modelMap = new HashMap<>();
		
		PlayLisenter playLisenter = ObjectUtils.defaultIfNull(lisenter, PlayLisenter.NULL);
		
		BroadcastLisenter broadcastlisenter = 
				ObjectUtils.defaultIfNull(configuration.getBroadcastLisenter(), new DramaBroadcastLisenter());

		for (Event event : events) {
			if(playLisenter.onBeforePlay(event)) {
				break;
			}
			
			broadcastlisenter.setHandingStatus(HandingStatus.Transmit);
			
			Class<?> eventClazz = event.getClass();
			// 检查注册事件类型范围
			if (!eventClazz.equals(Event.class) && InheritanceUtils.distance(eventClazz, DramaEvent.class) == 0) {
				throw DramaException.illegalRegisterEvent(eventClazz);
			}

			playDeal(event, modelMap, broadcastlisenter);
			// 触发事件
			playLisenter.onCompletedPlay(event);
			
			if (Objects.equals(broadcastlisenter.getHandingStatus(), HandingStatus.Exit)) {
				break;
			}
		}

		currentRender.get().setCode(Render.SUCCESS);
		currentRender.get().setModel(modelMap);
		
		return currentRender.get();
	}

	@Override
	public Render play(Event... events) throws DramaException {
		return play(null, events);
	}
	
	protected void playDeal(Event event, Map<String, Object> modelMap, BroadcastLisenter lisenter) {
		getLogging().dealEvent(event);

		if (!(event instanceof DramaEvent)) {
			return;
		}

		DramaEvent<?> dramaEvent = (DramaEvent<?>) event;
		dramaEvent.setEventResult(new EventResult(dramaEvent));

		playDealEvent(dramaEvent, lisenter);

		EventResult eventResult = dramaEvent.getEventResult();
		Collection<EventResultValue> resultValues = eventResult.allResults();

		resultValues.stream().filter(r -> r.getOutput()).forEach(r -> {
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

			if (Objects.equals(lisenter.getHandingStatus(), HandingStatus.Exit)) {
				break;
			}
		}
	}

	@Override
	public void setup(Configuration configuration) throws DramaException {
		this.configuration = Objects.requireNonNull(configuration);
		this.kernel = Objects.requireNonNull(configuration.getKernel());

		// 清空逻辑处理层，以便可以重新获取
		layers = null;
		
		logging = LoggingTemplateFactory.getStageLoggingTemplate(configuration.getLoggingFactory());		
		logging.setup(this);
		
		// 获取注册事件工厂
		final RegisterEventFactory registerEventFactory = configuration.getRegisterEventFactory();
		
		if(Objects.isNull(registerEventFactory)) {
			throw DramaException.emptyRegisterEvents();
		}
		// 获取注册元素工厂
		final RegisterElementFactory registerElementFactory = configuration.getRegisterElementFactory();
		
		if(Objects.isNull(registerElementFactory)) {
			throw DramaException.emptyRegisterElements();
		}
		// 自定义逻辑处理层构建工厂
		final LayerFactory layerFacotry = configuration.getLayerFactory();
		
		// 注册逻辑处理层
		final List<LayerDescriptor> layerDescList = new ArrayList<>();

		if(Objects.isNull(kernel.getLayerGenerator())) {
			addLayerGenerator(layerFacotry, layerDescList);
		}
		
		if(!registerEvent(registerEventFactory.events())) {
			throw DramaException.errorRegisterEvents();
		}
		
		if(!registerElement(registerElementFactory.elements())) {
			throw DramaException.emptyRegisterElements();
		}
		// 打印注册了哪些逻辑处理层
		layerDescList.forEach(desc -> getLogging().regeisteredLayer(new String[] { desc.getName() }));
	}

	protected void addLayerGenerator(final LayerFactory layerFacotry, final List<LayerDescriptor> layerDescList) {
		kernel.setLayerGenerator((p) -> {
			Layer layer;

			if(Objects.equals(Layer.Null.class, p.getParam1())) {
				layer = on(DEFAULT_LAYER_CLASS, this.getClass().getClassLoader()).create().get();
			}else if(Objects.isNull(layerFacotry)) {
				return null;
			} else {
				layer = layerFacotry.getLayer(p.getParam1());

				if(Objects.isNull(layer)) {
					layer = layerFacotry.getLayer(p.getParam2());
				}
			}

			if(Objects.nonNull(layer) && !layerDescList.contains(p.getParam2())) {
				layerDescList.add(p.getParam2());
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
			
			if(Objects.isNull(layer)) {
				return false;
			}
			
			layer.setConfiguration(configuration);
			
			getLogging().regeisteredElement(new Class<?>[] {element.getClass()});
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
}

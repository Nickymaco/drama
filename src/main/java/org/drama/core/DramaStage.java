package org.drama.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.InheritanceUtils;
import org.drama.annotation.AliasAnno;
import org.drama.collections.ImmutableSet;
import org.drama.event.AbstractEvent;
import org.drama.event.Event;
import org.drama.event.EventResult;
import org.drama.event.EventResultEntity;
import org.drama.event.EventResultValue;
import org.drama.exception.OccurredException;
import org.drama.log.LoggingFactory;
import org.drama.log.template.IStageLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

/**
 * 默认舞台，执行逻辑处理层时按照线性关系依次执行
 */
public class DramaStage implements Stage {
	private final Kernel kernel = new DramaKernel();
	private final ThreadLocal<StageRender> currentRender = new ThreadLocal<>();
	
	private IStageLoggingTemplate logging;
	private LoggingFactory loggingFactory;
	private LayerFactory layerFactory;
	private ImmutableSet<Layer> layers;
	
	private RegisterElementFactory registerElementFactory;
	private RegisterEventFactory registerEventFactory;

	@Override
	public final Kernel getKernel() {
		return kernel;
	}

	public LayerFactory getLayerFactory() {
		return layerFactory;
	}
	
	@Override
	public ImmutableSet<Layer> getLayers() {
		if(Objects.isNull(layers)) {
			layers = getKernel().getlayers();
		}
		return layers;
	}
	
	protected IStageLoggingTemplate getLogging() {
		return logging;
	}

	@Override
	public Render play(Event... events) throws OccurredException {
		currentRender.set(new StageRender());

		if (ArrayUtils.isEmpty(events)) {
			currentRender.get().setCode(Render.FAILURE);
			currentRender.get().setMessage(Render.UnfoundEventMsg);
			currentRender.get().setModel(null);
			return currentRender.get();
		}

		getLogging().logRecevieEvent(events);

		Map<String, Object> modelMap = new HashMap<>();

		for (Event event : events) {
			Class<?> eventClazz = event.getClass();
			// 检查注册事件类型范围
			if (!eventClazz.equals(Event.class) && InheritanceUtils.distance(eventClazz, AbstractEvent.class) == 0) {
				throw OccurredException.illegalRegisterEvent(eventClazz);
			}

			playDeal(event, modelMap);
		}

		currentRender.get().setCode(Render.SUCCESS);
		currentRender.get().setModel(modelMap);
		
		return currentRender.get();
	}
	
	protected void playDeal(Event event, Map<String, Object> modelMap) throws OccurredException {
		getLogging().logDealEvent(event);

		if (!(event instanceof AbstractEvent)) {
			return;
		}

		AbstractEvent<?> abstractEvent = (AbstractEvent<?>) event;
		abstractEvent.setEventResult(new EventResult(abstractEvent));

		playDealEvent(abstractEvent);

		EventResult eventResult = abstractEvent.getEventResult();
		Collection<EventResultValue> resultValues = eventResult.allResults();

		resultValues.stream().filter((r) -> r.isOutput()).forEach((r) -> {
			Class<?> clzR = r.getValue().getClass();
			AliasAnno aliasName = clzR.getAnnotation(AliasAnno.class);

			if (aliasName != null && StringUtils.isNotBlank(aliasName.value())) {
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
	 * 默认严格策略，只要有一个逻辑处理层返回不成功则不继续往下执行
	 */
	protected void playDealEvent(Event event) throws OccurredException {
		if (event == null) {
			return;
		}

		if (getLayers() == null || getLayers().size() == 0) {
			return;
		}

		for (Layer layer : getLayers()) {
			BroadcastResult broadcastResult = null;

			try {
				broadcastResult = layer.broadcast(event);
			} catch (OccurredException e) {
				throw OccurredException.occurredPlayError(e, event);
			}

			if (broadcastResult.getStatus() == BroadcastTracer.Completed) {
				currentRender.get().setCode(Render.SUCCESS);
				currentRender.get().setMessage(Render.AbendMsg);
				break;
			}
		}
	}

	@Override
	public void setLayerFactory(final LayerFactory layerFactory) {
		this.layerFactory = layerFactory;
		
		getKernel().addLayerGenerator((p) -> {
			if(Objects.isNull(layerFactory)) {
				return null;
			}
			
			Layer layer = null;
			
			layer = layerFactory.getLayer(p.getParam1());
			
			if(Objects.isNull(layer)) {
				layer = layerFactory.getLayer(p.getParam2().name(), p.getParam2().priority());
			}
			
			return layer;
		});
	}

	private void setLogging(IStageLoggingTemplate logging) {
		this.logging = logging;
	}

	@Override
	public void setLoggingFactory(LoggingFactory loggingFactory) {
		this.loggingFactory = loggingFactory;
		setLogging(LoggingTemplateFactory.getStageLoggingTemplate(loggingFactory));
	}

	@Override
	public void setRegisterElementFactory(RegisterElementFactory registerElementFactory) {
		this.registerElementFactory = registerElementFactory;
	}

	@Override
	public void setRegisterEventFactory(RegisterEventFactory registerEventFactory) {
		this.registerEventFactory = registerEventFactory;
	}

	@Override
	public void setup() throws OccurredException {
		if(Objects.isNull(registerEventFactory)) {
			throw OccurredException.emptyRegisterEvents();
		}
		
		if(Objects.isNull(registerElementFactory)) {
			throw OccurredException.emptyRegisterElements();
		}
		
		if(registerEvent(registerEventFactory.events())) {
			throw OccurredException.errorRegisterEvents();
		}
		
		if(registerElement(registerElementFactory.elements())) {
			throw OccurredException.emptyRegisterElements();
		}
	}
	
	protected boolean registerElement(Set<Element> elements) {
		if (CollectionUtils.isEmpty(elements)) {
			return false;
		}
		
		for (Element element : elements) {
			Layer layer = getKernel().registerElement(element);
			
			if(Objects.isNull(layer)) {
				return false;
			}
			
			layer.setLoggingFactory(loggingFactory);
		}
		return true;
	}

	protected boolean registerEvent(Set<Class<? extends Event>> events) {
		if (CollectionUtils.isEmpty(events)) {
			return false;
		}

		getKernel().regeisterEvent(events);
		
		return true;
	}
}

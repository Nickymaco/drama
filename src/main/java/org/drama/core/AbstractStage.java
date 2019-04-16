package org.drama.core;

import static org.drama.delegate.Delegator.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.InheritanceUtils;
import org.drama.annotation.ElementProperty;
import org.drama.annotation.EventResultProperties;
import org.drama.common.MessageTemplate;
import org.drama.event.AbstractEvent;
import org.drama.event.Event;
import org.drama.event.EventResult;
import org.drama.event.EventResultEntity;
import org.drama.event.EventResultValue;
import org.drama.exception.OccurredException;
import org.drama.log.template.IStageLoggingTemplate;

/**
 * 抽象舞台，每个具体舞台都应该衍生于它
 */
public abstract class AbstractStage implements Stage, StagePlayNotification {
	private Consumer<Event> prePalyAction;
    private Consumer<Event> afterPlayAction;
    private final ThreadLocal<StageRender> currentRender = new ThreadLocal<>();

    @Override
    public void addLayer(Layer layer) {
    	getLayers().add(layer);
    }

    @Override
    public void removeLayer(Layer layer) {
    	getLayers().remove(layer);
    }

    @Override
	public void prePlay(Consumer<Event> consumer) {
    	prePalyAction = consumer;
	}

	@Override
	public void afterPlay(Consumer<Event> consumer) {
		afterPlayAction = consumer;
	}

	@Override
	public Render play(Event... events) throws OccurredException {
        if(events == null || events.length == 0) {
            return new StageRender(Render.WARNING, true, null, MessageTemplate.inst().getRenderUnfoundEvent());
        }
        
        this.getStageLoggingTemplate().logRecevieEvent(events);
        
        StageRender render = new StageRender();
        
        setCurrentRender(render);

        Map<String, Object> modelMap = new HashMap<>();

        for(Event event : events) {
        	Class<?> eventClazz = event.getClass();
        	// 检查注册事件类型范围
            if(!eventClazz.equals(Event.class) && InheritanceUtils.distance(eventClazz, AbstractEvent.class) == 0) {
                throw OccurredException.illegalRegisterEvent(eventClazz);
            }
            // play开始通知
            action(this.prePalyAction, event);

            playDeal(event, modelMap);

            // play结束通知
            action(this.afterPlayAction, event);
        }

        render.setModel(modelMap);
        return render;
	}

    private void playDeal(Event event, Map<String, Object> modelMap) throws OccurredException {
    	this.getStageLoggingTemplate().logDealEvent(event);
    	
        if(!(event instanceof AbstractEvent)) {
            return;
        }
        
        AbstractEvent<?> abstractEvent = (AbstractEvent<?>)event;
        abstractEvent.setEventResult(new EventResult(abstractEvent));

        playDeal(abstractEvent);

        EventResult eventResult = abstractEvent.getEventResult();
        Collection<EventResultValue> resultValues = eventResult.allResults();
        
        resultValues.stream()
        	.filter((r) -> r.isOutput())
        	.forEach((r) -> {
        		Class<?> clzR = r.getValue().getClass();
        		EventResultProperties prop = clzR.getAnnotation(EventResultProperties.class);
        		
        		if(prop != null && StringUtils.isNotBlank(prop.alias())) {
        			modelMap.put(prop.alias(), r.getValue());
        		} else {
        			EventResultEntity entity = r.getValue();
        			Class<?> clzEntity = entity.getClass();
        			String simpleName = clzEntity.getSimpleName();
        			modelMap.put(simpleName, entity);
        		}
        	});
    }
    
    @Override
	public void registerElement(Element... elements) throws OccurredException {
    	if(elements == null || elements.length == 0) {
    		return;
    	}
    	
    	for(Element element : elements) {
	    	ElementProperty property = element.getClass().getAnnotation(ElementProperty.class);
	    	
	    	if(property == null) {
	    		return;
	    	}
	    	
	    	Class<? extends Layer> registerLayer = property.registerLayer();
	    	Layer layer = getLayers().stream().filter((l) -> l.getClass().equals(registerLayer)).findFirst().get();
	    	
	    	if(layer == null) {
	    		return;
	    	}
	    	
	    	layer.addElement(element);
    	}
	}

    /**
     * 具体事件通知
     * @param event
     * @param stageContext
     * @throws Exception
     */
    public abstract void playDeal(Event event) throws OccurredException;
    
    /**
     * 获取日志
     * @return
     */
    protected abstract IStageLoggingTemplate getStageLoggingTemplate();
    
    private void setCurrentRender(StageRender render) {
    	this.currentRender.set(render);
    }

	public StageRender getCurrentRender() {
		return currentRender.get();
	}
}

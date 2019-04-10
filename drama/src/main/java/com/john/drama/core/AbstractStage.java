package com.john.drama.core;

import static com.john.drama.delegate.Delegator.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.john.drama.annotation.ElementProperty;
import com.john.drama.event.AbstractEvent;
import com.john.drama.event.Event;
import com.john.drama.event.EventResult;
import com.john.drama.event.EventResultValue;
import com.john.drama.exception.OccurredException;
import com.john.drama.log.template.IStageLoggingTemplate;

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
            return new StageRender(Render.WARNING, true, null, "找不到需要处理的事件");
        }
        
        this.getStageLoggingTemplate().logRecevieEvent(events);
        
        StageRender render = new StageRender();
        
        setCurrentRender(render);

        Map<String, Object> modelMap = new HashMap<>();

        for(Event event : events) {
        	Class<?> eventClazz = event.getClass();
        	Class<?> supperClass = eventClazz.getSuperclass();
        	// 检查注册事件类型范围
            if(!eventClazz.equals(Event.class) && !supperClass.equals(AbstractEvent.class) && !supperClass.equals(Event.class)) {
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
        
        AbstractEvent abstractEvent = (AbstractEvent)event;
        abstractEvent.setEventResult(new EventResult(abstractEvent));

        playDeal(abstractEvent);

        EventResult eventResult = abstractEvent.getEventResult();
        Collection<EventResultValue> resultValues = eventResult.allResults();
        
        resultValues.stream()
        	.filter((r) -> r.isOutput())
        	.forEach((r) -> modelMap.put(r.getKey(), r.getValue()));
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
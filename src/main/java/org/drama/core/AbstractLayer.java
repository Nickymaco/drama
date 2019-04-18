package org.drama.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.drama.annotation.ElementProperty;
import org.drama.event.Event;
import org.drama.exception.OccurredException;
import org.drama.log.Logging;
import org.drama.log.LoggingAdapter;
import org.drama.log.template.ILayerLoggingTemplate;
import org.drama.log.template.LoggingTemplateFactory;

/**
 * 抽象逻辑处理层，每个逻辑处理层应该派生于它
 */
public abstract class AbstractLayer implements Layer {
    private static Map<ElementEventIndex, Set<Element>> eventElemPool;
    private final List<ElementNotification> elemNotifyList = new ArrayList<>();
    private ILayerLoggingTemplate layerLogging;

    static {
    	eventElemPool = new HashMap<>();
    }

    @Override
    public void addElement(Element element) throws OccurredException {
    	ElementProperty elemProp = element.getClass().getAnnotation(ElementProperty.class);
    	
    	if(elemProp == null || elemProp.registerEvent() == null || elemProp.registerEvent().length == 0) {
    		return;
    	}
    	
    	addElement(element, elemProp);
    }

    protected void addElement(Element element, ElementProperty elemProp) throws OccurredException { 	
    	Class<? extends Event>[] events = elemProp.registerEvent();
    	
    	Set<Element> elementSet;
    	ElementEventIndex elemEventIdx;
    	
    	for(int i=0, j=events.length; i < j; i++) {
    		elemEventIdx = new ElementEventIndex(events[i], this.getClass());
    		elementSet = eventElemPool.get(elemEventIdx);
    		
    		if(elementSet == null) {
            	elementSet = new TreeSet<>();
                eventElemPool.put(elemEventIdx, elementSet);
            }
    		
    		elementSet.add(element);
    	}
        
        addElementNotification(element);
    }

	protected void addElementNotification(Element element) {
		if(!(element instanceof ElementNotification)) {
			return;
		}
		
		ElementNotification elemNotify = (ElementNotification)element;
		
		if(!elemNotifyList.contains(elemNotify)) {
			elemNotifyList.add(elemNotify);
		}
	}

    @Override
    public BroadcastResult broadcast(Event event) throws OccurredException {
		BroadcastResult result = new BroadcastResult(BroadcastTracer.Processing);
		
		if(event == null) {
		    throw OccurredException.illegalBroadcastEvent(this, event);
		}
		
		// 设置当前逻辑处理层
		event.getContext().setCurrentLayer(this);
		// 打印日志
		getLogging().logBroadcast(this, event);
		
		handingElement(event, result);
		
		notifyLayerCompleted(event);
		
        return result;
    }

	protected void handingElement(Event event, BroadcastResult result) throws OccurredException {
		Set<Element> elemSet = eventElemPool.get(new ElementEventIndex(event.getClass(), this.getClass()));
		
		if(elemSet == null || elemSet.size() == 0) {
			notifyLayerCompleted(event);
			return;
		}
		
		for(Element elem : elemSet) {
			try {
				elem.handing(event);
			} catch (Exception e) {
				throw OccurredException.occurredHandingError(e, this, elem);
			}
			// 结束当前逻辑处理层
			if(elem.cancelable() == Broken.Layer) {
				break;
			}
			// 退出舞台表演
			if(elem.cancelable() == Broken.Stage) {
				result.setStatus(BroadcastTracer.Completed);
				break;
			}
		}
	}

	protected void notifyLayerCompleted(Event event) {
		elemNotifyList.stream().forEach((elem) -> elem.onLayerCompleted(event));
	}

	protected ILayerLoggingTemplate getLogging() {
    	if(layerLogging == null) {
    		Logging logging = LoggingAdapter.delegateLogging(() -> null);
    		layerLogging = LoggingTemplateFactory.getLayerLoggingTemplate(logging);
    	}
    	return layerLogging;
    }

    protected void setLogging(Logging logging) {
    	if(layerLogging == null || layerLogging.getLogging() == null || !layerLogging.getLogging().equals(logging)) {
    		layerLogging = LoggingTemplateFactory.getLayerLoggingTemplate(logging);
    	}
    }

	protected List<ElementNotification> getElemNotifyList() {
		return elemNotifyList;
	}
}

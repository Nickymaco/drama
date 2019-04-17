package org.drama.core;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.drama.vo.TriParameterValueObject;

/**
 * 抽象逻辑处理层，每个逻辑处理层应该派生于它
 */
public abstract class AbstractLayer implements Layer {
	static class ElementComparator 
		implements Comparable<TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>>> {
		
		private TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>> raw;
		
		public ElementComparator(TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>> raw) {
			this.raw = raw;
		}

		@Override
		public int compareTo(TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>> o) {
			if(o == null || !(o instanceof TriParameterValueObject<?, ?, ?>)) {
				return -1;
			}
			
			try {
				if(this.raw.getParam1().getPriority() > o.getParam1().getPriority()) {
					return 1;
				} else if(this.raw.getParam1().getPriority() < o.getParam1().getPriority()) {
					return -1;
				} else {
					return 0;
				}
			} catch (Exception e) {
				return 0;
			}
		}
	}
	
    private static Map<String, Set<TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>>>> pool;
    private final List<ElementNotification> elemNotifyList = new ArrayList<>();
    private ILayerLoggingTemplate layerLogging;
    private Class<?> thisClazz = this.getClass();
    

    static {
    	pool = new HashMap<>();
    }

    @Override
    public void addElement(Element element) throws OccurredException {
    	ElementProperty elemProp = element.getClass().getAnnotation(ElementProperty.class);
    	
    	if(elemProp == null) {
    		return;
    	}
    	
    	addElement(element, elemProp);
    }

    protected void addElement(Element element, ElementProperty elemProp) throws OccurredException {
    	String delegateName = thisClazz.getSimpleName();
    	Set<TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>>> elementSet = pool.get(delegateName);
    	
    	if(elementSet == null) {
        	elementSet = new TreeSet<>();
            pool.put(delegateName, elementSet);
        }

    	TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>> param = 
    			new TriParameterValueObject<>(element, elemProp, Arrays.asList(elemProp.registerEvent()));
    	
    	param.setCompareDelegate(new ElementComparator(param));
    	
        elementSet.add(param);
        
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
    public int compareTo(Layer o) {
        if(o == null) {
            return 1;
        } else if (o.getPriority() == this.getPriority()) {
            return 0;
        } else {
            return o.getPriority() > this.getPriority() ? -1 : 1;
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
		this.getLogging().logBroadcast(this, event);
		
		String delegateName = thisClazz.getSimpleName();
		Set<TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>>> paramSet = pool.get(delegateName);
		
		if(paramSet == null || paramSet.size() == 0) {
			return result;
		}
		
		for(TriParameterValueObject<Element, ElementProperty, List<Class<? extends Event>>> param : paramSet) {
			List<Class<? extends Event>> events = param.getParam3();
			
			if(!events.contains(event.getClass()) && !events.contains(Event.class)) {
				continue;
			}
			
			Element elem = param.getParam1();
			
			try {
				elem.handing(event);
			} catch (Exception e) {
				throw OccurredException.occurredHandingError(e, this, elem);
			}
			
			if(elem.cancelable() == Broken.Layer) {
				break;
			}
			if(elem.cancelable() == Broken.Stage) {
				result.setStatus(BroadcastTracer.Completed);
				break;
			}
		}
		
		notifyLayerCompleted(event);
		
        return result;
    }

	protected void notifyLayerCompleted(Event event) {
		elemNotifyList.stream().forEach((elem) -> elem.onLayerCompleted(event));
	}

	protected ILayerLoggingTemplate getLogging() {
    	if(this.layerLogging == null) {
    		this.layerLogging = 
    				LoggingTemplateFactory.getLayerLoggingTemplate(LoggingAdapter.delegateLogging(() -> null));
    	}
    	return this.layerLogging;
    }

    protected void setLogging(Logging logging) {
    	if(this.layerLogging == null || this.layerLogging.getLogging() == null || !this.layerLogging.getLogging().equals(logging)) {
    		this.layerLogging = LoggingTemplateFactory.getLayerLoggingTemplate(logging);
    	}
    }

	protected List<ElementNotification> getElemNotifyList() {
		return elemNotifyList;
	}
}

package org.drama.event;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drama.core.Layer;

public class EventContext implements Serializable {
	private static final long serialVersionUID = 7285410254463046346L;
	private Map<Object, Object> context = new ConcurrentHashMap<>();
	private Layer currentLayer;
	private Event currentEvent;
	
	public EventContext() {
	}
	
	public EventContext(Event event) {
		setCurrentEvent(event);
	}
	
	public void addParameter(Object key, Object value) {
       context.put(key, value);
    }

    public Object getParameter(Object key) throws Exception {
        Object value = this.context.get(key);

        if(value == null) {
            return null;
        }

        return value;
    }

	public Layer getCurrentLayer() {
		return currentLayer;
	}

	public void setCurrentLayer(Layer currentLayer) {
		this.currentLayer = currentLayer;
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}
}

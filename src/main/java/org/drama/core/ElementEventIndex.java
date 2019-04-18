package org.drama.core;

import org.drama.event.Event;
import org.drama.event.EventIndex;

class ElementEventIndex implements EventIndex {
	private Class<? extends Event> eventClazz;
	private Class<? extends Layer> layerClazz;
	
	protected ElementEventIndex() {
	}
	
	public ElementEventIndex(Class<? extends Event> eventClz, Class<? extends Layer> layerClz) {
		setEventClazz(eventClz);
		setLayerClazz(layerClz);
	}

	@Override
	public Class<? extends Event> getEventClazz() {
		return eventClazz;
	}

	@Override
	public Class<?> getSourceClazz() {
		return layerClazz;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(getEventClazz() == null || layerClazz == null) {
			return false;
		}
		
		if(obj == null) {
			return false;
		}
		
		if(!(obj instanceof EventIndex)) {
			return false;
		}
		
		EventIndex index = (EventIndex)obj;
		
		if(index.getEventClazz() == null || index.getSourceClazz() == null) {
			return false;
		}
		
		return index.getEventClazz().equals(getEventClazz()) && index.getSourceClazz().equals(layerClazz);
	}

	protected void setEventClazz(Class<? extends Event> eventClazz) {
		this.eventClazz = eventClazz;
	}

	protected void setLayerClazz(Class<? extends Layer> layerClazz) {
		this.layerClazz = layerClazz;
	}
}

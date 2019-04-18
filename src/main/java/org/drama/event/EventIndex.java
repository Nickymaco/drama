package org.drama.event;

public interface EventIndex {
	Class<? extends Event> getEventClazz();
	Class<?> getSourceClazz();
}

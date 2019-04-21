package org.drama.core;

import java.util.Set;

import org.drama.event.Event;

public interface RegisterEventFactory {
	Set<Class<? extends Event>> events();
}

package org.drama.core;

import org.drama.event.Event;

import java.util.Set;

public interface RegisterEventFactory {
    Set<Class<? extends Event>> events();
}

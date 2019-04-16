package org.drama.core;

import org.drama.event.Event;

public interface ElementNotification {
	void onLayerCompleted(Event event);
	void onStageCompleted(Event event);
}

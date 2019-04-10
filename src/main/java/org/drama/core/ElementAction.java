package org.drama.core;

import org.drama.event.EventContext;

public interface ElementAction {
	void preHanding(EventContext eventContext);
	void afterHanding(EventContext eventContext);
}

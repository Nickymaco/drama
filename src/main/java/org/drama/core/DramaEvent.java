package org.drama.core;

import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventContext;
import org.drama.event.EventResult;

/**
 * 抽象事件，所以事件都应该派生于它
 */
public abstract class DramaEvent<T> implements Event{
	private ThreadLocal<EventArgument<T>> argument = new ThreadLocal<>();
    private ThreadLocal<EventResult> eventResult = new ThreadLocal<>();
    private ThreadLocal<EventContext> contextLocal = new ThreadLocal<>();
    
    public DramaEvent() {
    	contextLocal.set(new EventContext(this));
    }
    
    @Override
	public EventContext getContext() {
		return contextLocal.get();
	}

    @Override
    public EventResult getEventResult() {
        return eventResult.get();
    }

    public void setEventResult(EventResult eventResult) {
        this.eventResult.set(eventResult);
    }

    @Override
    public EventArgument<T> getArgument() {
        return argument.get();
    }

    public void setArgument(EventArgument<T> argument) {
        this.argument.set(argument);
    }
}

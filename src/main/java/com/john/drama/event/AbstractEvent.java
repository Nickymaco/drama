package com.john.drama.event;

/**
 * 抽象事件，所以事件都应该派生于它
 */
public abstract class AbstractEvent implements Event{
	private EventArgument<?> argument;
    private EventResult eventResult;
    private ThreadLocal<EventContext> contextLocal;
    
    public AbstractEvent() {
    	contextLocal = new ThreadLocal<>();
    	contextLocal.set(new EventContext());
    }
    
    @Override
	public EventContext getContext() {
		return contextLocal.get();
	}

    @Override
    public EventResult getEventResult() {
        return eventResult;
    }

    public void setEventResult(EventResult eventResult) {
        this.eventResult = eventResult;
    }

    @Override
    public EventArgument<?> getArgument() {
        return this.argument;
    }

    public void setArgument(EventArgument<?> argument) {
        this.argument = argument;
    }
}

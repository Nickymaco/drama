package org.drama.core;

import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventContext;
import org.drama.event.EventResult;

import javax.annotation.PreDestroy;

/**
 * 抽象事件，事件都应派生于它
 * @param <T> 限定事件参数类型
 */
public abstract class DramaEvent<T> implements Event<T> {
    private EventArgument<T> argument;
    private final DramaEventResult eventResult;
    private final DramaEventContext context;

    public DramaEvent(){
        this(new DramaEventResult(), new DramaEventContext());
    }

    public DramaEvent(DramaEventResult eventResult, DramaEventContext context) {
        this.eventResult = eventResult;
        this.context = context;
    }

    @Override
    public EventContext getContext() {
        return context;
    }

    @Override
    public EventResult getEventResult() {
        return eventResult;
    }

    @Override
    public EventArgument<T> getArgument() {
        return argument;
    }

    @Override
    public void setArgument(EventArgument<T> argument) {
        this.argument = argument;
    }

    @PreDestroy
    private void destroy() {
        eventResult.destroy();
        context.destroy();
    }
}

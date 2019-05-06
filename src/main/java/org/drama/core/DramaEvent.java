package org.drama.core;

import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventContext;
import org.drama.event.EventResult;

/**
 * 默认事件对象，自定义事件可以应派生于它
 */
public class DramaEvent implements Event {
	private static final long serialVersionUID = 4016926097748852570L;
	private EventArgument<?> argument;
    private final DramaEventResult eventResult;
    private final DramaEventContext context;
    private String name;

    public DramaEvent(String name) {
        this(name, new DramaEventResult(), new DramaEventContext());
    }

    public DramaEvent(String name, DramaEventResult eventResult, DramaEventContext context) {
        this.name = name;
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
    public EventArgument<?> getArgument() {
        return argument;
    }

    @Override
    public void setArgument(EventArgument<?> argument) {
        this.argument = argument;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void close() throws Exception {
        eventResult.destroy();
        context.destroy();
    }
}

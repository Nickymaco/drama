package org.drama.event;

import org.drama.core.DramaEvent;

import static org.joor.Reflect.on;

public class EventBuilder<T extends DramaEvent<TT>, TT> {
    private Class<T> clazz;
    private EventArgument<TT> argument;

    public EventBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public EventBuilder<T, TT> setArgument(TT value) {
        EventArgument<TT> argument = new EventArgument<>();
        argument.setArgument(value);
        this.argument = argument;
        return this;
    }

    public T build(Object... paramters) throws Throwable {
        T tEvent;
        tEvent = on(clazz).create(paramters).get();
        tEvent.setArgument(this.argument);
        return tEvent;
    }
}

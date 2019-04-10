package com.john.drama.event;

import java.lang.reflect.Constructor;

public class EventBuilder<T extends AbstractEvent> {
    private Class<T> clazz;
    private EventArgument<?> argument;

    public EventBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public <V> EventBuilder<T> setArgument(V value) {
        if(value.getClass().equals(EventArgument.class)) {
            this.argument = (EventArgument<?>)value;
        } else {
            EventArgument<V> argument = new EventArgument<>();
            argument.setArgument(value);
            this.argument = argument;
        }
        return this;
    }

    public T build(Object... paramters) {
        T tEvent = null;

        try {
            if(paramters != null && paramters.length > 0) {
                Class<?>[] paramterTypes = new Class<?>[paramters.length];

                for (int i = 0; i < paramterTypes.length; i++) {
                    paramterTypes[i] = paramters[0].getClass();
                }

                Constructor<T> constructor = this.clazz.getDeclaredConstructor(paramterTypes);
                tEvent = constructor.newInstance(paramters);
            } else {
                tEvent = this.clazz.newInstance();
            }

            tEvent.setArgument(this.argument);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tEvent;
    }
}

package org.drama.event;

import static org.drama.delegate.Delegator.forEach;
import static org.joor.Reflect.on;

import org.apache.commons.lang3.ArrayUtils;
import org.drama.vo.KeyValueObject;
import org.joor.Reflect;

public class EventBuilder {
    private Class<? extends Event> clazz;
    private EventArgument<?> argument;
    private Object[] parameters;
    private KeyValueObject<String, Object>[] properties;

    public EventBuilder setType(Class<? extends Event> clazz) {
        this.clazz = clazz;
        return this;
    }

    public <T> EventBuilder setArgument(T value) {
        EventArgument<T> argument = new EventArgument<>();
        argument.setArgument(value);
        this.argument = argument;
        return this;
    }

    public EventBuilder setArgument(EventArgument<?> argument) {
        this.argument = argument;
        return this;
    }

    public EventBuilder setParameters(Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public EventBuilder setProperties(KeyValueObject<String, Object>[] properties) {
        this.properties = properties;
        return this;
    }

    public Event build() {
        final Reflect reflect;

        if (!ArrayUtils.isEmpty(parameters)) {
            reflect = on(clazz).create(parameters);
        } else {
            reflect = on(clazz).create();
        }

        if (ArrayUtils.isNotEmpty(properties)) {
            forEach(properties, (property, i) -> {
                reflect.set(property.getKey(), property.getValue());
                return false;
            });
        }

        Event tEvent = reflect.get();
        tEvent.setArgument(argument);
        return tEvent;
    }

    public boolean reset() {
        clazz = null;
        argument = null;
        parameters = null;
        properties = null;
        return true;
    }
}

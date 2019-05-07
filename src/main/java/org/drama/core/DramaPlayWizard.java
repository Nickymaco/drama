package org.drama.core;

import static org.drama.delegate.Delegator.func;

import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.drama.annotation.EventProperty;
import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventBuilder;
import org.drama.vo.KeyValueObject;

class DramaPlayWizard implements PlayWizard {
    private Stage stage;
    private String eventName;
    private Object[] parameters;
    private Class<? extends Event> clazz;
    private EventArgument<?> argument;
    private BroadcastListener broadcastListener;
    private PlayListener playLisenter;
    private KeyValueObject<String, Object>[] properties;
    private EventBuilder builder = new EventBuilder();
    private Function<String, Class<? extends Event>> classFactory;

    DramaPlayWizard(Stage stage, Function<String, Class<? extends Event>> classFactory) {
        this.stage = stage;
        this.classFactory = ObjectUtils.defaultIfNull(classFactory, (name) -> null);
    }

    @Override
    public PlayWizard event(String name) {
        this.eventName = name;
        this.clazz = func(classFactory, name);
        return this;
    }

    @Override
    public PlayWizard event(Class<? extends Event> clazz) {
        this.clazz = clazz;
        EventProperty eventProperty = clazz.getAnnotation(EventProperty.class);

        if(Objects.nonNull(eventProperty) && ArrayUtils.isNotEmpty(eventProperty.aliasFor())) {
            if(!ArrayUtils.contains(eventProperty.aliasFor(), this.eventName)) {
                this.eventName = clazz.getSimpleName();
            }
        }
        return this;
    }

    @Override
    public <T> PlayWizard argument(T argument) {
        EventArgument<T> eventArgument = new EventArgument<>();
        eventArgument.setArgument(argument);
        this.argument = eventArgument;
        return this;
    }

    @Override
    public PlayWizard parameters(Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    @Override
    public PlayWizard properties(KeyValueObject<String, Object>[] properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public PlayWizard broadcastLisenter(BroadcastListener lisenter) {
        this.broadcastListener = lisenter;
        return this;
    }

    @Override
    public PlayWizard playListener(PlayListener lisenter) {
        this.playLisenter = lisenter;
        return this;
    }

    @Override
    public Render play() {
        Event event = buidEvent();
        return stage.play(new Event[]{event}, this.playLisenter, this.broadcastListener);
    }

    @Override
    public void play(Render render) {
        Event event = buidEvent();
        stage.play(render, new Event[]{event}, this.playLisenter, this.broadcastListener);
    }

    private Event buidEvent() {
        builder.reset();

        Event event;

        if (Objects.isNull(clazz)) {
            event = new DramaEvent();
            event.setArgument(this.argument);
        } else {
	        event = builder
	                .setType(clazz)
	                .setArgument(argument)
	                .setParameters(parameters)
	                .setProperties(properties)
	                .build();
        }
        
        if(Objects.isNull(event.getName()) && StringUtils.isNoneBlank(this.eventName)) {
        	event.setName(this.eventName);
        }
        return event;
    }
}

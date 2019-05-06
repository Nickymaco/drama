package org.drama.core;

import org.apache.commons.lang3.ObjectUtils;
import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventBuilder;
import org.drama.vo.KeyValueObject;

import java.util.Objects;
import java.util.function.Function;

import static org.drama.delegate.Delegator.func;

class DramaPlayWizard implements PlayWizard {
    private Stage stage;
    private String eventName;
    private Object[] parameters;
    private Class<? extends Event> clazz;
    private EventArgument<?> argument;
    private BroadcastListener broadcastListener;
    private PlayLisenter playLisenter;
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
    public PlayWizard playLisenter(PlayLisenter lisenter) {
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

        if (Objects.isNull(clazz)) {
            DramaEvent event = new DramaEvent(eventName);
            event.setArgument(this.argument);
            return event;
        }

        return builder
                .setType(clazz)
                .setArgument(argument)
                .setParameters(parameters)
                .setProperties(properties)
                .build();
    }
}

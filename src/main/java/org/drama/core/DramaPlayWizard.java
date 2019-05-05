package org.drama.core;

import org.apache.commons.lang3.ObjectUtils;
import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventBuilder;
import org.drama.exception.DramaException;
import org.drama.vo.KeyValueObject;

import java.util.Objects;
import java.util.function.Function;

import static org.drama.delegate.Delegator.func;

class DramaPlayWizard implements PlayWizard {
    private Stage stage;
    private Object[] parameters;
    private Class<? extends Event> clazz;
    private EventArgument<?> argument;
    private BroadcastLisenter broadcastLisenter;
    private PlayLisenter playLisenter;
    private KeyValueObject<String, Object>[] properties;
    private EventBuilder builder = new EventBuilder();
    private Function<String, Class<? extends Event>> funcGetEvenClass;

    DramaPlayWizard(Stage stage, Function<String, Class<? extends Event>> funcGetEvenClass) {
        this.stage = stage;
        this.funcGetEvenClass = ObjectUtils.defaultIfNull(funcGetEvenClass, (name) -> null);
    }

    @Override
    public PlayWizard event(String name) {
        this.clazz = func(funcGetEvenClass, name);
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
    public PlayWizard broadcastLisenter(BroadcastLisenter lisenter) {
        this.broadcastLisenter = lisenter;
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
        return stage.play(new Event[]{event}, this.playLisenter, this.broadcastLisenter);
    }

    @Override
    public void play(Render render) {
        Event event = buidEvent();
        stage.play(render, new Event[]{event}, this.playLisenter, this.broadcastLisenter);
    }

    private Event buidEvent() {
        if(Objects.isNull(clazz)) {
            throw DramaException.emptyRegisterEvents();
        }

        return builder
                .setType(clazz)
                .setArgument(argument)
                .setParameters(parameters)
                .setProperties(properties)
                .build();
    }
}

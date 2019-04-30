package org.drama.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.vo.KeyValueObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.forEach;

final class LayerContainer implements Comparable<LayerContainer> {
    private final static Map<KeyValueObject<Class<? extends Event>, LayerContainer>, Runnable> handingMap = new ConcurrentHashMap<>();
    private final UUID identity;
    private final Layer layer;
    private String name;
    private int priority;
    private boolean disabled = false;
    private Class<? extends Event>[] excludeEvent;
    private final Set<ElementContainer> elementContainers;
    private Set<Element> elements;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    LayerContainer(Layer layer, UUID identity, String name, int priority, Class<? extends Event>... events) {
        this.identity = identity;
        this.layer = layer;
        this.excludeEvent = ObjectUtils.defaultIfNull(events, (Class<? extends Event>[])new Class<?>[]{});
        this.elementContainers = new TreeSet<>();
        setName(name);
        setPriority(priority);
    }

    @Override
    public int compareTo(LayerContainer o) {
        return Integer.compare(getPriority(), o.getPriority());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public UUID getIdentity() {
        return identity;
    }

    public Layer getLayer() {
        return layer;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisable(boolean disabled) {
        if (Objects.equals(this.disabled, disabled)) {
            return;
        }
        this.disabled = disabled;
    }

    public Class<? extends Event>[] getExcludeEvent() {
        return excludeEvent;
    }

    public void setExcludeEvent(Class<? extends Event>[] excludeEvent) {
        this.excludeEvent = excludeEvent;
    }

    public ImmutableSet<Class<? extends Event>> getRegeisteredEvents() {
        return ImmutableSet.newInstance(
                handingMap.keySet().stream().map(KeyValueObject::getKey).collect(Collectors.toSet()));
    }

    public ImmutableSet<Element> getElements() {
        if(Objects.isNull(elements)) {
            elements = new LinkedHashSet<>();
            elementContainers.forEach(elemCon -> elements.add(elemCon.getInvocator()));
        }
        return ImmutableSet.newInstance(elements);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(identity);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj) || !Objects.equals(obj.getClass(), getClass())) {
            return false;
        }

        LayerContainer that = (LayerContainer) obj;

        return Objects.equals(identity, that.identity);
    }

    public void addElement(ElementContainer element) {
        elementContainers.add(element);
    }

    public void handingEevnt(Event event, final Consumer<LayerContainer> onPreHanding, final Consumer<ElementContainer> onCompleted) {
        if(disabled) {
            return;
        }

        final Class<? extends Event> eventClass = event.getClass();

        if(ArrayUtils.contains(excludeEvent, eventClass)) {
            return;
        }

        KeyValueObject<Class<? extends Event>, LayerContainer> handingKey = new KeyValueObject<>(eventClass, this);

        Runnable handing = handingMap.get(handingKey);

        if(Objects.nonNull(handing)) {
            action(handing);
            return;
        }

        final Set<ElementContainer> handingSet = new TreeSet<>();

        elementContainers.stream().filter(elem -> {
            Class<? extends Event>[] events = elem.getRegisterEvents();

           if(ArrayUtils.isEmpty(events)) {
               return false;
           }

           return ArrayUtils.contains(events, eventClass) || elem.getGlobal();
        }).forEach(handingSet::add);

        final LayerContainer that = this;

        handingMap.put(handingKey, handing = () -> {
            if(CollectionUtils.isEmpty(handingSet)) {
                return;
            }

            action(onPreHanding, that);

            forEach(handingSet, (elemCon, i) -> {
                elemCon.setCurrentLayer(that.layer);

                Element elem = elemCon.getInvocator();

                elem.handing(event);

                action(onCompleted, elemCon);

                elemCon.setCurrentLayer(null);

                return !Objects.equals(elem.getBroadcastStatus(), BroadcastStatus.Transmit);
            });
        });

        action(handing);
    }
}

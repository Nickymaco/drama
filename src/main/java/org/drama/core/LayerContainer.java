package org.drama.core;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.forEach;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.vo.KeyValueObject;

final class LayerContainer implements Comparable<LayerContainer> {
    private final static Map<KeyValueObject<String, LayerContainer>, Runnable> handingMap = new ConcurrentHashMap<>();
    private final UUID identity;
    private final Layer layer;
    private String name;
    private int priority;
    private boolean disabled = false;
    private String[] excludeEvent;
    private final Set<ElementContainer> elementContainers;
    private Set<Element> elements;

    LayerContainer(Layer layer, UUID identity, String name, int priority, String[] excludeEvents) {
        this.identity = identity;
        this.layer = layer;
        this.excludeEvent = excludeEvents;
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

    public String[] getExcludeEvent() {
        return excludeEvent;
    }

    public void setExcludeEvent(String[] excludeEvent) {
        this.excludeEvent = excludeEvent;
    }

    public ImmutableSet<String> getRegeisteredEvents() {
        return ImmutableSet.newInstance(
                handingMap.keySet().stream().map(KeyValueObject::getKey).collect(Collectors.toSet()));
    }

    public ImmutableSet<Element> getElements() {
        if (Objects.isNull(elements)) {
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

    public void handingEevnt(final Event event, final Consumer<LayerContainer> onPreHanding, final Consumer<ElementContainer> onCompleted) {
        if (disabled || ArrayUtils.contains(excludeEvent, event.getName())) {
            return;
        }

        KeyValueObject<String, LayerContainer> handingKey = new KeyValueObject<>(event.getName(), this);

        Runnable handing = handingMap.get(handingKey);

        if (Objects.nonNull(handing)) {
            action(handing);
            return;
        }

        Set<ElementContainer> handingSet = new TreeSet<>();

        elementContainers.stream()
                .filter(e -> e.getRegisterEvents().contains(event.getName()) || e.getGlobal())
                .forEach(handingSet::add);

        handingMap.put(handingKey, handing = () -> {
            if (CollectionUtils.isEmpty(handingSet)) {
                return;
            }

            action(onPreHanding, this);

            forEach(handingSet, (elemCon, i) -> {
                Element elem = elemCon.getInvocator();

                elem.handing(event);

                action(onCompleted, elemCon);

                return !Objects.equals(elem.getBroadcastStatus(), BroadcastStatus.Transmit);
            });
        });

        action(handing);
    }
}

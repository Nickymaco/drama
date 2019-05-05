package org.drama.core;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.annotation.ElementProperty;
import org.drama.exception.DramaException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

final class ElementContainer implements InvocationHandler, Comparable<ElementContainer> {
    private final int priority;
    private final Element elem;
    private final Object invodicator;
    private final String name;
    private final String simpleName;
    private final boolean global;
    private final Set<String> registerEvents;
    private final ElementProperty elementProperty;

    ElementContainer(Element element) {
        this.elem = element;

        Class<?> clazz = element.getClass();
        name = clazz.getName();
        simpleName = clazz.getSimpleName();

        ElementProperty prop = Objects.requireNonNull(clazz.getAnnotation(ElementProperty.class));
        this.elementProperty = prop;
        this.priority = prop.priority();
        this.global = prop.any();
        this.registerEvents = new HashSet<>(Arrays.asList(prop.events()));

        if (!prop.any() && ArrayUtils.isEmpty(prop.events())) {
            throw DramaException.emptyRegisterEvents();
        }

        Class<?>[] interfaces = clazz.getInterfaces();

        if (!ArrayUtils.contains(interfaces, Element.class)) {
            interfaces = ArrayUtils.add(interfaces, Element.class);
        }

        invodicator = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, this);
    }

    public int getPriority() {
        return priority;
    }

    public Element getInvocator() {
        return (Element) invodicator;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(elem);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj) || !Objects.equals(getClass(), obj.getClass())) {
            return false;
        }

        ElementContainer that = (ElementContainer) obj;

        return Objects.equals(elem, that.elem);
    }

    @Override
    public int compareTo(ElementContainer o) {
        if (Objects.isNull(o)) {
            return 1;
        } else {
            return Integer.compare(getPriority(), o.getPriority());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(elem, args);
        } catch (Exception e) {
            throw DramaException.occurredHandingError(e, elem);
        }
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public BroadcastStatus getHandingStatus() {
        return getInvocator().getBroadcastStatus();
    }

    public boolean getGlobal() {
        return global;
    }

    public Set<String> getRegisterEvents() {
        return registerEvents;
    }

    public ElementProperty getElementProperty() {
        return elementProperty;
    }
}

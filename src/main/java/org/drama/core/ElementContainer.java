package org.drama.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.exception.OccurredException;

final class ElementContainer implements InvocationHandler, Comparable<ElementContainer> {
	private int priority;
	private final Element elem;
	private final Object invodicator;
	private Layer currentLayer;

	public ElementContainer(Element element) {
		elem = Objects.requireNonNull(element);

		Class<?> clazz = element.getClass();
		Class<?>[] interfaces = clazz.getInterfaces();

		if (!ArrayUtils.contains(interfaces, Element.class)) {
			interfaces = ArrayUtils.add(interfaces, Element.class);
		}

		invodicator = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, this);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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
		if (Objects.isNull(obj) || !(obj instanceof ElementContainer)) {
			return false;
		}

		ElementContainer elemCon = (ElementContainer) obj;

		return Objects.equals(elem, elemCon.getInvocator());
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
			throw OccurredException.occurredHandingError(e, currentLayer, elem);
		}
	}

	public Layer getCurrentLayer() {
		return currentLayer;
	}

	public void setCurrentLayer(Layer currentLayer) {
		this.currentLayer = currentLayer;
	}
}

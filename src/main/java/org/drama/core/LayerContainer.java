package org.drama.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;

final class LayerContainer implements InvocationHandler, Comparable<LayerContainer> {
	private final UUID indentity;
	private final Layer layer;
	private String name;
	private int priority;
	private boolean disabled = false;
	
	protected LayerContainer(Layer layer, UUID identity) {
		this(layer, identity, "", 0);
	}

	protected LayerContainer(Layer layer, UUID indentity, String name, int priority) {
		this.indentity = indentity;
		this.layer = layer;
		setName(name);
		setPriority(priority);
	}

	@Override
	public int compareTo(LayerContainer o) {
		return Integer.compare(getPriority(), o.getPriority());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(this.layer, args);
	}

	public <T extends Layer> Layer packaging() {
		Class<?> clazz = layer.getClass();
		return (Layer) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
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

	public UUID getIndentity() {
		return indentity;
	}

	public Layer getLayer() {
		return layer;
	}
	
	public boolean getDisabled() {
		return disabled;
	}

	public void setDisable(boolean disabled) {
		if(Objects.equals(this.disabled, disabled)) {
			return;
		}
		this.disabled = disabled;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.indentity);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj) || !(obj instanceof LayerContainer)) {
			return false;
		}
		
		LayerContainer container = (LayerContainer)obj;
		
		return Objects.equals(getIndentity(), container.getIndentity());
	}

}
package org.drama.core;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;

final class LayerContainer implements Comparable<LayerContainer> {
	private final UUID identity;
	private final Layer layer;
	private String name;
	private int priority;
	private boolean disabled = false;

	protected LayerContainer(Layer layer, UUID identity, String name, int priority) {
		this.identity = identity;
		this.layer = layer;
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
		if(Objects.equals(this.disabled, disabled)) {
			return;
		}
		this.disabled = disabled;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(identity);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj) || !Objects.equals(obj.getClass(), getClass())) {
			return false;
		}
		
		LayerContainer that = (LayerContainer)obj;
		
		return Objects.equals(identity, that.identity);
	}

}

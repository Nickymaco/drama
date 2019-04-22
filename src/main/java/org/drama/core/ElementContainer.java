package org.drama.core;

import java.util.Objects;

import org.apache.commons.lang3.builder.HashCodeBuilder;

class ElementContainer {
	private int priority;
	private final Element invocator;

	public ElementContainer(Element element) {
		invocator = Objects.requireNonNull(element);
	}


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


	public Element getInvocator() {
		return invocator;
	}


	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(invocator);
		return hcb.toHashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj) || !(obj instanceof ElementContainer)) {
			return false;
		}
		
		ElementContainer elemCon = (ElementContainer)obj;
		
		return Objects.equals(invocator, elemCon.getInvocator());
	}
}

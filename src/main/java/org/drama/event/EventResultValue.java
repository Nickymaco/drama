package org.drama.event;

import java.io.Serializable;

/**
 * 事件结果值包装器
 */
public class EventResultValue implements Serializable {
	private static final long serialVersionUID = -3647983190859217981L;
	private EventResultEntity value;
    private boolean isOutput;
    
	public EventResultValue(EventResultEntity value) {
		this(value, false);
	}
	
	public EventResultValue(EventResultEntity value, boolean isOutput) {
		this.setValue(value);
		this.isOutput = isOutput;
	}

	public boolean isOutput() {
		return isOutput;
	}

	public void setOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}

	public EventResultEntity getValue() {
		return value;
	}

	public void setValue(EventResultEntity value) {
		this.value = value;
	}
}

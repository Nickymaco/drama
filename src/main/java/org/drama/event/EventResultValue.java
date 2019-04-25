package org.drama.event;

import java.io.Serializable;

/**
 * 事件结果值包装器
 */
public class EventResultValue implements Serializable {
	private static final long serialVersionUID = -3647983190859217981L;
	private EventResultEntity value;
    private boolean output;
    
	public EventResultValue(EventResultEntity value) {
		this(value, false);
	}
	
	public EventResultValue(EventResultEntity value, boolean output) {
		this.setValue(value);
		this.output = output;
	}

	public boolean getOutput() {
		return output;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

	public EventResultEntity getValue() {
		return value;
	}

	public void setValue(EventResultEntity value) {
		this.value = value;
	}
}

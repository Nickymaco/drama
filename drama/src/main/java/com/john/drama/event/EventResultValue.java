package com.john.drama.event;

import java.io.Serializable;

import com.john.drama.vo.KeyValueObject;

/**
 * 事件结果值包装器
 */
public class EventResultValue implements Serializable {
	private static final long serialVersionUID = -3647983190859217981L;
	private KeyValueObject<String, Object> value;
    private boolean isOutput = false;
    
	public EventResultValue(KeyValueObject<String, Object> value) {
		this.value = value;
	}
	
	public EventResultValue(KeyValueObject<String, Object> value, boolean isOutput) {
		this.value = value;
		this.isOutput = isOutput;
	}

	public String getKey() {
		return this.value.getKey();
	}
	
	public Object getValue() {
		return this.value.getValue();
	}

	public boolean isOutput() {
		return isOutput;
	}

	public void setOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}
}

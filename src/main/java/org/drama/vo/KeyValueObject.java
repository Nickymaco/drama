package org.drama.vo;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class KeyValueObject<K,V> implements Serializable {
	private static final long serialVersionUID = 2344632851099497825L;
	
	private K key;
	private V value;
	
	public KeyValueObject() {
	}
	
	public KeyValueObject(K key, V value) {
		setKey(key);
		setValue(value);
	}
	
	public K getKey() {
		return key;
	}
	
	public void setKey(K key) {
		this.key = key;
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(key);
		hcb.append(value);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(Objects.isNull(obj) || !(obj instanceof KeyValueObject<?,?>)) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		KeyValueObject<K, V> kv = (KeyValueObject<K, V>)obj;
		
		return Objects.equals(getKey(), kv.getKey()) && Objects.equals(getValue(), kv.getValue());
	}
	
	
}

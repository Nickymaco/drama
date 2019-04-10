package org.drama.vo;

import java.io.Serializable;

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
}

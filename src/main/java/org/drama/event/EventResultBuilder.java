package org.drama.event;

import org.drama.vo.KeyValueObject;

public class EventResultBuilder {
	private Class<? extends Event> eventMeta;
	private Class<?> sourceMeta;
	private String namespace;
	private String group;
	private String artifact;
	private Object result;
	private EventResultIndex resultIndex;
	private EventResultValue resultvalue;
	
	public EventResultBuilder() {
	}
	
	public EventResultBuilder(String namespace) {
		this.namespace = namespace;
	}
	
	public EventResultBuilder(String namespace, String group) {
		this(namespace);
		this.group = group;
	}
	
	public EventResultBuilder setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}
	
	public EventResultBuilder setGroup(String group) {
		this.group = group;
		return this;
	}
	
	public EventResultBuilder setEventMeta(Class<? extends Event> meta) {
		this.eventMeta = meta;
		return this;
	}
	
	public EventResultBuilder setSourceMeta(Class<?> meta) {
		this.sourceMeta = meta;
		return this;
	}
	
	public EventResultBuilder setArtifact(String artifact) {
		this.artifact = artifact;
		return this;
	}
	
	public EventResultIndex getResultIndex() {
		return resultIndex;
	}

	public EventResultValue getResultvalue() {
		return resultvalue;
	}
	
	public EventResultBuilder setResult(Object result) {
		this.result = result;
		return this;
	}
	
	public void build(EventResult eventResult, boolean output) {
		resultIndex = new EventResultIndex(eventMeta, sourceMeta, namespace, group, artifact);
		resultvalue = new EventResultValue(new KeyValueObject<>(artifact, this.result), output);
		
		eventResult.addResult(resultIndex, resultvalue);
	}
}

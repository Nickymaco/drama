package org.drama.event;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * 事件结果索引，对事件结果做一个逻辑上的业务元数据描述
 * @author john
 */
public class EventResultIndex implements Serializable {
	private static final long serialVersionUID = -7453443690549858336L;
	private static final String DEFAULT_NAMESPACE="Drama";
	private static final String DEFAULT_GROUP="Default";

	private Class<? extends Event> eventMeta;
	private Class<?> sourceMeta;
	private String namespace;
	private String group;
	private String artifact;
	
	public EventResultIndex(Class<? extends Event> t1, Class<?> t2) {
		setEventMeta(t1);
		setSourceMeta(t2);
	}
	
	public EventResultIndex(Class<? extends Event> t1, Class<?> t2, String namespace, String group) {
		this(t1, t2);
		setNamespace(namespace);
		setGroup(group);
	}
	
	public EventResultIndex(Class<? extends Event> t1, Class<?> t2, String namespace, String group, String artifact) {
		this(t1, t2, namespace, group);
		setArtifact(artifact);
	}
	
	public Class<? extends Event> getEventMeta() {
		return eventMeta;
	}
	
	public void setEventMeta(Class<? extends Event> eventMeta) {
		this.eventMeta = eventMeta;
	}
	
	public Class<?> getSourceMeta() {
		return sourceMeta;
	}
	
	public void setSourceMeta(Class<?> sourceMeta) {
		this.sourceMeta = sourceMeta;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof EventResultIndex)) {
			return false;
		}
		
		EventResultIndex newObj = (EventResultIndex)obj;
		
		return newObj.getEventMeta().equals(this.getEventMeta())
				&& newObj.getSourceMeta().equals(this.getSourceMeta())
				&& newObj.getNamespace().equals(this.getNamespace())
				&& newObj.getGroup().equals(this.getGroup());
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(String.format("%s::", StringUtils.isBlank(getNamespace()) ? DEFAULT_NAMESPACE : getNamespace()));
		buff.append(String.format("%s::", StringUtils.isBlank(getGroup()) ? DEFAULT_GROUP : getGroup()));
		buff.append(String.format("%s::", getEventMeta().getSimpleName()));
		buff.append(getSourceMeta().getSimpleName());
		
		if(StringUtils.isNoneBlank(getArtifact())) {
			buff.append(String.format("::%s", getArtifact()));
		}
		
		return buff.toString();
	}
}

package org.drama.event;

import java.io.Serializable;
import java.util.UUID;

/**
 * 事件结果索引，对事件结果做一个逻辑上的业务元数据描述
 * @author john
 */
public class EventResultIndex implements Serializable {
	private static final long serialVersionUID = -7453443690549858336L;
	private Class<? extends Event> eventMeta;
	private Class<?> sourceMeta;
	private UUID artifactId;
	
	public EventResultIndex(Class<? extends Event> eventMeta, Class<?> sourceMeta) {
		this(eventMeta, sourceMeta, new UUID(0,0));
	}
	
	
	public EventResultIndex(Class<? extends Event> eventMeta, Class<?> sourceMeta, UUID artifact) {
		setArtifactId(artifact);
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
	
	public UUID getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(UUID artifact) {
		this.artifactId = artifact;
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
				&& newObj.getArtifactId().equals(this.getArtifactId());
	}

	@Override
	public String toString() {
		return String.format("%s[%s]::$s", eventMeta.getSimpleName(), artifactId, sourceMeta.getSimpleName());
	}
}

package org.drama.event;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 事件结果索引，对事件结果做一个逻辑上的业务元数据描述
 *
 * @author john
 */
public class EventResultIndex implements Serializable {
    private static final long serialVersionUID = -7453443690549858336L;
    private Class<? extends Event> eventMeta;
    private Class<?> sourceMeta;
    private UUID artifactId;

    public EventResultIndex(UUID artifact, Class<? extends Event> eventMeta, Class<?> sourceMeta) {
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
        this.artifactId = Objects.requireNonNull(artifact);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(artifactId);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj) || !Objects.equals(getClass(), obj.getClass())) {
            return false;
        }

        EventResultIndex that = (EventResultIndex) obj;

        return Objects.equals(that.artifactId, artifactId);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]::$s", eventMeta.getSimpleName(), artifactId, sourceMeta.getSimpleName());
    }
}

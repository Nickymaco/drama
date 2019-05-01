package org.drama.event;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.security.Signature;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * 事件结果索引，对事件结果做一个逻辑上的业务元数据描述
 */
public class EventResultIndex implements Serializable {
    private static final long serialVersionUID = -7453443690549858336L;
    private static final String RESULT_INDEX = "EventResultIndex-%s[%s]";
    private final Signature signature;

    public EventResultIndex(final String uuid, final Event event) {
        this.signature = new Signature() {
            @Override
            public UUID getIdentity() {
                return UUID.fromString(uuid);
            }

            @Override
            public Serializable getSigner() {
                return event;
            }
        };
    }

    public Signature getSignature() {
        return signature;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(signature.getIdentity());
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj) || !Objects.equals(getClass(), obj.getClass())) {
            return false;
        }

        EventResultIndex that = (EventResultIndex) obj;

        return Objects.nonNull(that.signature) && Objects.equals(that.signature.getIdentity(), signature.getIdentity());
    }

    @Override
    public String toString() {
        return String.format(RESULT_INDEX, signature.getSigner().getClass().getSimpleName(), signature.getIdentity());
    }
}

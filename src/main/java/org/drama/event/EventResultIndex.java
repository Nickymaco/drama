package org.drama.event;

import static org.drama.text.MessageText.format;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.drama.security.Signature;

/**
 * 事件结果索引，对事件结果做一个逻辑上的业务元数据描述
 */
public class EventResultIndex implements Serializable {
    private static final long serialVersionUID = -7453443690549858336L;
    private static final String RESULT_INDEX = "EventResultIndex-{0}[{1}]";
    private final Signature signature;

    public EventResultIndex(String uuid, Event event) {
        signature = new Signature() {
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
        return format(RESULT_INDEX, signature.getSigner(), signature.getIdentity());
    }
}

package org.drama.event;

import java.io.Serializable;

/**
 * 空接口，用于限定事件结果是一个实体避免基础类型
 */
public interface EventResultEntity extends Serializable {
    default String aliasName() {
        return "";
    }
}

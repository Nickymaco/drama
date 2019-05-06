package org.drama.event;

import static org.drama.text.Symbol.EMPTY;

import java.io.Serializable;;

/**
 * 事件结果实体定义接口
 */
public interface EventResultEntity extends Serializable {
    default String aliasName() {
        return EMPTY;
    }
}

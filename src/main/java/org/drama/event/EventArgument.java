package org.drama.event;

import java.io.Serializable;

/**
 * 事件参数对象
 *
 * @param <T> 参数值类型
 */
public class EventArgument<T> implements Serializable {
    private static final long serialVersionUID = -2894669687322825883L;

    private T argument;

    public T getArgument() {
        return argument;
    }

    public void setArgument(T argument) {
        this.argument = argument;
    }
}

package org.drama.event;

import java.io.Serializable;

/**
 * 事件定义
 */
public interface Event extends Serializable, AutoCloseable {
    /**
     * 获取事件名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 设置事件名称
     * @param name 名称
     */
    void setName(String name);

    /**
     * 获取事件参数
     */
    EventArgument<?> getArgument();

    /**
     * 设置时间参数
     *
     * @param argument 参数
     */
    void setArgument(EventArgument<?> argument);

    /**
     * 获取事件结果
     */
    EventResult getEventResult();

    /**
     * 事件上下文
     */
    EventContext getContext();
}

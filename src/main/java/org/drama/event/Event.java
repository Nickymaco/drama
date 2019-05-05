package org.drama.event;

import java.io.Serializable;

/**
 * 事件，可以作为全局事件注册，注册全局事件后其他自定义事件不需要重复注册，为互斥
 */
public interface Event extends Serializable {
    /**
     * 获取事件名称
     *
     * @return 事件名称
     */
    default String getName() {
        return getClass().getSimpleName();
    }

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

package org.drama.event;

/**
 * 事件，可以作为全局事件注册，注册全局事件后其他自定义事件不需要重复注册，为互斥
 */
public interface Event {
    /**
     * 获取事件参数
     *
     *
     */
    EventArgument<?> getArgument();

    /**
     * 获取事件结果
     *
     */
    EventResult getEventResult();
    
    /**
     * 事件上下文
     *
     */
    EventContext getContext();
}

package org.drama.event;

/**
 * 事件
 */
public interface Event {
    /**
     * 获取事件参数
     *
     * @return
     */
    EventArgument<?> getArgument();

    /**
     * 获取事件结果
     * @return
     */
    EventResult getEventResult();
    
    /**
     * 事件上下文
     * @return
     */
    EventContext getContext();
}

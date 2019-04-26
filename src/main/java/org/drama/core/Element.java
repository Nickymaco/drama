package org.drama.core;

import org.drama.event.Event;

/**
 * 逻辑处理层事件句柄
 */
public interface Element {
    /**
     * 获取当前元素处理状态
     *
     */
    default HandingStatus getHandingStatus() {
    	return HandingStatus.Transmit;
    }
    
    /**
     * 触发事件关联行为
     * @param event
     */
    void handing(Event event);
}

package com.john.drama.core;

import com.john.drama.event.Event;

/**
 * 逻辑处理层事件句柄
 */
public interface Element extends Comparable<Object> {
    /**
     * 优先级
     * @return
     */
    int getPriority();
    /**
     * 告知逻辑处理层是否继续往下执行
     * @return
     */
    default Broken cancelable(){ return Broken.None; }

    /**
     * 触发事件关联行为
     * @param event
     */
    void handing(Event event, Layer layer);

    default int compareTo(Object o) {
        return 0;
    }
}

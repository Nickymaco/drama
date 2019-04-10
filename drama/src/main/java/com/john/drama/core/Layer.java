package com.john.drama.core;

import com.john.drama.event.Event;
import com.john.drama.exception.OccurredException;

/**
 * 逻辑处理层，每一个逻辑处理层负责发起通知
 */
public interface Layer extends Comparable<Layer> {
    /**
     * 逻辑处理层优先级，用于在舞台优先走到那个逻辑处理层
     * @return
     */
    int getPriority();

    /**
     * 注册事件
     * @param event
     * @param element
     */
    void addElement(Element element) throws OccurredException;

    /**
     * 广播事件
     */
    BroadcastResult broadcast(Event event) throws OccurredException;
}

package org.drama.log.template;

import org.drama.event.Event;
import org.drama.log.LoggingInteractor;

public interface ILayerLoggingTemplate extends LoggingInteractor {
    /**
     * 记录传播事件
     *
     * @param layerName 逻辑处理层名称
     * @param event     事件
     */
    void broadcast(String layerName, Event event);

    /**
     * 记录元素handing
     *
     * @param elementName 已handing过的元素
     */
    void handingElement(String elementName);
}
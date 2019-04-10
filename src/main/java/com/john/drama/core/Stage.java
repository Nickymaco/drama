package com.john.drama.core;

import java.util.Set;

import com.john.drama.event.Event;
import com.john.drama.exception.OccurredException;

/**
 * 事件舞台
 */
public interface Stage {
    /**
     * 加人逻辑处理层
     * @param layer
     */
    void addLayer(Layer layer);

    /**
     * 去掉逻辑处理层
     * @param layer
     */
    void removeLayer(Layer layer);
    
    /**
     * 注册元素
     * @param element
     */
    void registerElement(Element... element) throws OccurredException;

    /**
     * 获取逻辑处理层
     * @return
     */
    Set<Layer> getLayers();

    /**
     * 演出
     */
    Render play(Event... events) throws OccurredException;
    
    /**
     * 默认异常输出
     * @return
     */
    static Render defaultErrorRender() {
    	return new StageRender(Render.ERROR, false, null, "服务发生异常");
    }
}

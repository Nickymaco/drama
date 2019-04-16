package org.drama.core;

import java.util.List;
import java.util.Set;

import org.drama.common.MessageTemplate;
import org.drama.event.Event;
import org.drama.exception.OccurredException;

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
     * 批量增加逻辑处理层
     * @param layers
     */
    default void addLayer(List<Layer> layers) {
    	layers.stream().forEach((l) -> addLayer(l));
    }

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
    	return new StageRender(Render.FAILURE, null, MessageTemplate.inst().getRenderError());
    }
}

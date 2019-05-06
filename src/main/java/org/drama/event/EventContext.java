package org.drama.event;

import java.io.Serializable;

import org.drama.core.Layer;

public interface EventContext extends Serializable {
    /**
     * 事件上下文添加参数
     *
     * @param key
     * @param value
     */
    void addParameter(String key, Object value);

    /**
     * 获取事件向下文参数
     *
     * @param key
     * @return 返回参数值
     * @throws Exception
     */
    Object getParameter(String key);

    /**
     * 当前逻辑处理层
     *
     * @return 逻辑层处理层
     */
    Layer getCurrentLayer();

    /**
     * 设置当前逻辑处理层
     *
     * @param currentLayer 逻辑处理层
     */
    void setCurrentLayer(Layer currentLayer);
}

package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.OccurredException;

/**
 * 事件舞台
 */
public interface Stage {
    /**
     * 获取逻辑处理层，这里应该返回只读集合
     *
     */
    ImmutableSet<Layer> getLayers();
    /**
     * 演出
     */
    Render play(Event... events) throws OccurredException;
    /**
     * 演出
     * @param lisenter
     * @param events
     *
     * @throws OccurredException
     */
    Render play(PlayLisenter lisenter, Event... events) throws OccurredException;
    /**
     * 参数配置
     *
     */
    Configuration getConfiguration();
    /**
     * 启动
     */
    void setup(Configuration configuration) throws OccurredException;
    /**
     * 默认异常输出
     *
     */
    static Render defaultErrorRender() {
    	return new StageRender(Render.FAILURE, null, Render.ERROR_MSG);
    }
}

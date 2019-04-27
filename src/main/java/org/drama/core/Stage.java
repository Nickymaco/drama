package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.DramaException;

/**
 * 事件舞台
 */
public interface Stage {
    /**
     * 获取逻辑处理层，这里应该返回只读集合
     */
    ImmutableSet<Layer> getLayers();

    /**
     * 演出
     * @param events 事件
     * @return 渲染
     * @throws DramaException
     */
    Render play(Event[] events) throws DramaException;

    /**
     * 演出
     * @param events 事件
     * @param playLisenter Stage play 监听器
     * @param broadcastLisenter 逻辑处理层广播监听器
     * @return 渲染
     */
    Render play(Event[] events, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter) throws DramaException;

    /**
     * 演出（自定义提供 Render）
     * @param render 自定义演出渲染
     * @param events 事件
     * @param playLisenter Stag play 监听器
     * @param broadcastLisenter 逻辑处理层广播监听器
     * @throws DramaException
     */
    void play(Render render, Event[] events, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter) throws DramaException;

    /**
     * 参数配置
     */
    Configuration getConfiguration();

    /**
     * 启动
     */
    void setup(Configuration configuration) throws DramaException;
}

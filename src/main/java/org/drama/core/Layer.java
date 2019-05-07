package org.drama.core;

import org.drama.event.Event;

/**
 * 逻辑处理层，负责发起事件广播，每个逻辑处理层必须保证有一个无参数构造函数
 */
public interface Layer {
    /**
     * 广播事件
     */
    void broadcast(Event event, BroadcastListener broadcasetListener);

    /**
     * 参数配置
     */
    Configuration getConfiguration();

    /**
     * 参数配置
     *
     * @param configuration 配置
     */
    void setConfiguration(Configuration configuration);
}

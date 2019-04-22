package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.log.LoggingFactory;

/**
 * 逻辑处理层，每一个逻辑处理层负责发起通知,实现接口时应该重写 equals 方法和 hashcode 方法
 */
public interface Layer {
	void setKernel(Kernel kernel);
    /**
     * 当前逻辑处理层有哪些注册元素
     * @return
     */
    ImmutableSet<Element> getElements();
    /**
     * 添加日志
     * @param loggingFactory
     */
    void setLoggingFactory(LoggingFactory loggingFactory);
    /**
     * 广播事件
     */
    void broadcast(Event event, BroadcastLisenter broadcasetListener);
}

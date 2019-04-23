package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.log.LoggingFactory;

/**
 * 逻辑处理层，每一个逻辑处理层负责发起通知,实现接口时应该重写 equals 方法和 hashcode 方法
 */
public interface Layer {
	static final String DefaultName = "Drama";
	static final String DefaultUUID = "A66A23C6-1A62-4B53-AD80-6DDB58D900D";
	static final int DefaultPriority = 3290;
	/**
	 * 提供内核
	 * @param kernel
	 */
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

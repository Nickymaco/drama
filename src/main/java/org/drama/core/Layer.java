package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;

/**
 * 逻辑处理层，每一个逻辑处理层负责发起通知,实现接口时应该重写 equals 方法和 hashcode 方法
 */
public interface Layer {
	static final String DefaultName = "Drama";
	static final String DefaultUUID = "A66A23C6-1A62-4B53-AD80-6DDB58D900D";
	static final int DefaultPriority = 3290;
	/**
	 * 占位符
	 * @author john
	 *
	 */
	static class Null implements Layer {
		@Override
		public ImmutableSet<Element> getElements() {
			return null;
		}
		@Override
		public void broadcast(Event event, BroadcastLisenter broadcasetListener) {
		}
		@Override
		public Configuration getConfiguration() {
			return null;
		}
		@Override
		public void setConfiguration(Configuration configuration) {
		}
	}
    /**
     * 当前逻辑处理层有哪些注册元素
     * @return
     */
    ImmutableSet<Element> getElements();
    /**
     * 广播事件
     */
    void broadcast(Event event, BroadcastLisenter broadcasetListener);
    
    /**
     * 参数配置
     * @return
     */
    Configuration getConfiguration();
    /**
     * 参数配置
     * @param configuration
     */
    void setConfiguration(Configuration configuration);
}

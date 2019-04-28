package org.drama.core;

import org.drama.event.Event;

/**
 * 逻辑处理层，负责发起事件广播，每个逻辑处理层必须保证有一个无参数构造函数
 */
public interface Layer {
    String DEFAULT_NAME = "Drama";
    String DEFAULT_UUID = "A66A23C6-1A62-4B53-AD80-6DDB58D900D";
    int DEFAULT_PRIORITY = 3290;

    /**
     * 占位符,防空指针
     */
    final class Null implements Layer {
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
     * 广播事件
     */
    void broadcast(Event event, BroadcastLisenter broadcasetListener);

    /**
     * 参数配置
     */
    Configuration getConfiguration();

    /**
     * 参数配置
     *
     * @param configuration
     */
    void setConfiguration(Configuration configuration);
}

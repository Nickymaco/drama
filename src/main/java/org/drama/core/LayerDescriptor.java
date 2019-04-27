package org.drama.core;

import org.drama.event.Event;

/**
 * 逻辑处理层描述符
 */
public interface LayerDescriptor {
    /**
     * 占位符，默认描述
     */
    enum Default implements LayerDescriptor {
        Drama(Layer.DEFAULT_NAME, Layer.DEFAULT_UUID, Layer.DEFAULT_PRIORITY);

        private String name;
        private String UUID;
        private int priority;

        Default(String name, String UUID, int priority) {
            this.name = name;
            this.UUID = UUID;
            this.priority = priority;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public String getUUID() {
            return UUID;
        }

        @Override
        public boolean getDisabled() {
            return false;
        }

        @Override
        public Class<? extends Event>[] getExculdeEvent() {
            return null;
        }
    }

    /**
     * 名称
     */
    String getName();

    /**
     * 优先级
     */
    int getPriority();

    /**
     * 唯一标识
     */
    String getUUID();

    /**
     * 禁用
     */
    boolean getDisabled();

    /**
     * 排除广播事件
     *
     * @return
     */
    Class<? extends Event>[] getExculdeEvent();
}

package org.drama.core;

/**
 * 逻辑处理层描述符
 */
public interface LayerDescriptor {
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
     * @return 排查事件名称
     */
    String[] getExculdeEvent();
}

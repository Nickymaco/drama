package org.drama.core;

public enum BroadcastStatus {
    /**
     * 持续广播
     */
    Transmit,
    /**
     * 逻辑处理层中断广播
     */
    Interrupt,
    /**
     * 退出本次事件整个广播
     */
    Exit
}

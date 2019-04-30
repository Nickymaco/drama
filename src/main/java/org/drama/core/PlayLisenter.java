package org.drama.core;

import org.drama.event.Event;

public interface PlayLisenter {
    /**
     * 占位符，防止抛空指针异常
     */
    PlayLisenter NULL = event -> {
    };

    /**
     * 在 Stage play 前触发
     *
     * @param event 事件
     * @return 返回{@code true}则退出，不会走到逻辑处理层广播
     */
    default boolean onBeforePlay(Event event) {
        return false;
    }

    /**
     * 在 Stage play 后触发
     *
     * @param event 事件
     */
    void onCompletedPlay(Event event);
}

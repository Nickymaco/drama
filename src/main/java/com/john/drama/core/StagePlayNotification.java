package com.john.drama.core;

import java.util.function.Consumer;

import com.john.drama.event.Event;

/**
 * 舞台事件
 */
public interface StagePlayNotification {
	/**
     * play前触发
     * @param consumer
     */
    void prePlay(Consumer<Event> consumer);
    
    /**
     * play后触发
     * @param consumer
     */
    void afterPlay(Consumer<Event> consumer);
}

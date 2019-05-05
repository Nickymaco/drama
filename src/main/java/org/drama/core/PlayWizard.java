package org.drama.core;

import org.drama.event.Event;
import org.drama.vo.KeyValueObject;

public interface PlayWizard {
    /**
     * 设置事件（Class or Name）
     *
     * @param name 事件名称
     */
    PlayWizard event(String name);

    /**
     * 设置事件（Class or Name）
     *
     * @param clazz 事件描述
     */
    PlayWizard event(Class<? extends Event> clazz);

    /**
     * 事件参数
     *
     * @param argument 事件参数
     * @param <T>      参数类型
     */
    <T> PlayWizard argument(T argument);

    /**
     * 事件构造参数，构造函数如果参数
     *
     * @param parameters 构造参数
     */
    PlayWizard parameters(Object[] parameters);

    /**
     * 事件属性
     *
     * @param properties 键值属性
     */
    PlayWizard properties(KeyValueObject<String, Object>[] properties);

    /**
     * 广播监听器
     *
     * @param lisenter 监听器
     */
    PlayWizard broadcastLisenter(BroadcastListener lisenter);

    /**
     * 演出监听器
     *
     * @param lisenter
     */
    PlayWizard playLisenter(PlayLisenter lisenter);

    /**
     * 演出
     *
     * @return 渲染
     */
    Render play();

    /**
     * 演出
     *
     * @param render 自定义渲染
     */
    void play(Render render);
}

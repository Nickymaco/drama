package org.drama.core;

import org.drama.event.Event;
import org.drama.vo.KeyValueObject;

public interface PlayWizard {
    /**
     * 设置事件，匹配已注册的自定义事件{@literal Class.SimpleName}，无则启用默认事件{@code DramaEvent}
     *
     * @param name 事件名称
     */
    PlayWizard event(String name);

    /**
     * 设置自定义事件
     *
     * @param clazz 事件{@literal Class}描述
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
     * @param listener 监听器
     */
    PlayWizard broadcastLisenter(BroadcastListener listener);

    /**
     * 演出监听器
     *
     * @param listener
     */
    PlayWizard playListener(PlayListener listener);

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

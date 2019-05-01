package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.DramaException;
import org.drama.vo.BiParameterValueObject;
import org.drama.vo.KeyValueObject;
import org.drama.vo.TriParameterValueObject;

/**
 * 事件舞台
 */
public interface Stage {
    /**
     * 获取逻辑处理层，这里应该返回只读集合
     */
    ImmutableSet<Layer> getLayers();

    /**
     * 演出
     *
     * @param event             已注册的事件名称
     * @param argument          事件参数
     * @param objects           事件构造函数参数
     * @param properties        事件属性设置
     * @param <T>               事件参数类型
     * @param playLisenter      演出监听器 (Option)
     * @param broadcastLisenter 广播监听器 (Option)
     * @return 渲染
     */
    @SuppressWarnings("unchecked")
    default <T> Render paly(String event, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter, T argument, Object[] objects, KeyValueObject<String, Object>... properties) {
        TriParameterValueObject<String, T, BiParameterValueObject<Object[], KeyValueObject<String, Object>[]>> param =
                new TriParameterValueObject<>(event, argument, new BiParameterValueObject<>(objects, properties));

        return play(new TriParameterValueObject[]{param}, playLisenter, broadcastLisenter);
    }

    /**
     * 演出
     *
     * @param objects 封装事件参数集合
     * @return 渲染
     */
    Render play(TriParameterValueObject<String, ?, BiParameterValueObject<Object[], KeyValueObject<String, Object>[]>>[] objects, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter);

    /**
     * 演出
     *
     * @param render            自定义渲染
     * @param event             已注册的事件名称
     * @param argument          事件参数
     * @param objects           事件构造函数参数  (Option)
     * @param <T>               事件参数类型
     * @param playLisenter      演出监听器 (Option)
     * @param broadcastLisenter 广播监听器 (Option)
     */
    default <T> void play(Render render, String event, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter, T argument, Object... objects) {
        paly(render, event, playLisenter, broadcastLisenter, argument, objects);
    }

    /**
     * 演出
     *
     * @param render            自定义渲染
     * @param event             已注册的事件名称
     * @param argument          事件参数
     * @param objects           事件构造函数参数
     * @param properties        事件属性设置
     * @param <T>               事件参数类型
     * @param playLisenter      演出监听器 (Option)
     * @param broadcastLisenter 广播监听器 (Option)
     */
    @SuppressWarnings("unchecked")
    default <T> void paly(Render render, String event, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter, T argument, Object[] objects, KeyValueObject<String, Object>... properties) {
        TriParameterValueObject<String, T, BiParameterValueObject<Object[], KeyValueObject<String, Object>[]>> param =
                new TriParameterValueObject<>(event, argument, new BiParameterValueObject<>(objects, properties));

        play(render, new TriParameterValueObject[]{param}, playLisenter, broadcastLisenter);
    }

    /**
     * 演出
     *
     * @param render            自定义渲染
     * @param objects           封装事件参数集合
     * @param playLisenter      演出监听器  (Option)
     * @param broadcastLisenter 广播监听器  (Option)
     */
    void play(Render render, TriParameterValueObject<String, ?, BiParameterValueObject<Object[], KeyValueObject<String, Object>[]>>[] objects, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter);

    /**
     * 演出
     *
     * @param events 事件
     * @return 渲染
     * @throws DramaException
     */
    Render play(Event[] events) throws DramaException;

    /**
     * 演出
     *
     * @param events            事件
     * @param playLisenter      Stage play 监听器 (Option)
     * @param broadcastLisenter 逻辑处理层广播监听器 (Option)
     * @return 渲染
     */
    Render play(Event[] events, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter) throws DramaException;

    /**
     * 演出（提供自定义 Render）
     *
     * @param render            自定义演出渲染
     * @param events            事件
     * @param playLisenter      Stag play 监听器 (Option)
     * @param broadcastLisenter 逻辑处理层广播监听器 (Option)
     * @throws DramaException
     */
    void play(Render render, Event[] events, PlayLisenter playLisenter, BroadcastLisenter broadcastLisenter) throws DramaException;

    /**
     * 参数配置
     */
    Configuration getConfiguration();

    /**
     * 启动
     */
    void setup(Configuration configuration) throws DramaException;
}

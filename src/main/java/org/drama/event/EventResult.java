package org.drama.event;

import java.io.Serializable;
import java.util.Collection;

public interface EventResult extends Serializable, AutoCloseable {
    /**
     * 增加事件结果
     *
     * @param index 结果索引
     * @param value 结果值
     */
    void addResult(EventResultIndex index, EventResultValue value);

    /**
     * 增加事件结果
     *
     * @param uuid  唯一id
     * @param event 关联事件
     * @param out   是否输出
     */
    void addResult(String uuid, Event event, EventResultEntity entity, boolean out);

    /**
     * 获取结果值
     *
     * @param key 索引
     * @return
     */
    EventResultValue getResult(EventResultIndex key);

    /**
     * 获取所有结果
     *
     * @return 结果集合
     */
    Collection<EventResultValue> allResults();
}

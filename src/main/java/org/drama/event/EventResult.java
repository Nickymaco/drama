package org.drama.event;

import org.drama.vo.KeyValueObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件结果
 */
public class EventResult implements Serializable {
    private static final long serialVersionUID = 4555755009367480736L;

    private Map<EventResultIndex, EventResultValue> objectMap = new ConcurrentHashMap<>();
    private Event event;

    public EventResult(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    /**
     * 添加事件结果
     *
     * @param param
     */
    public void addResult(KeyValueObject<EventResultIndex, EventResultValue> param) {
        objectMap.put(param.getKey(), param.getValue());
    }

    /**
     * 添加时间结果
     *
     * @param uuid ArtifactId
     * @param evt  事件
     * @param src  触发事件源
     * @param ent  事件结果
     */
    public void addResult(String uuid, Class<? extends Event> evt, Class<?> src, EventResultEntity ent, boolean out) {
        EventResultIndex index = new EventResultIndex(UUID.fromString(uuid), evt, src);
        EventResultValue value = new EventResultValue(ent, out);
        addResult(new KeyValueObject<>(index, value));
    }

    /**
     * 获取事件结果
     *
     * @param key
     */
    public EventResultValue getResult(EventResultIndex key) {
        return objectMap.get(key);
    }

    /**
     * 获取事件结果副本
     */
    public Collection<EventResultValue> allResults() {
        return objectMap.values();
    }
}

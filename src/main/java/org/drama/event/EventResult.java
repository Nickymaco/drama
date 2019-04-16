package org.drama.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.drama.vo.KeyValueObject;

/**
 * 事件结果
 */
public class EventResult implements Serializable {
    private static final long serialVersionUID = 4555755009367480736L;

    private Map<EventResultIndex, EventResultValue> objectMap = new HashMap<>();
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
     * @param key
     * @param value
     */
    public void addResult(KeyValueObject<EventResultIndex, EventResultValue> param) {
        objectMap.put(param.getKey(), param.getValue());
    }
    
    /**
     * 添加时间结果
     * @param uuid ArtifactId
     * @param evt 事件
     * @param src 触发石建元
     * @param ent 事件结果
     */
    public void addResult(String uuid, Class<? extends Event> evt, Class<?> src, EventResultEntity ent, boolean out) {
    	EventResultIndex index = new EventResultIndex(evt, src, UUID.fromString(uuid));
    	EventResultValue value = new EventResultValue(ent, out);
    	addResult(new KeyValueObject<>(index, value));
    }

    /**
     * 获取事件结果
     *
     * @param key
     * @return
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

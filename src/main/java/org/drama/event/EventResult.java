package org.drama.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public void addResult(EventResultIndex key, EventResultValue value) {
        objectMap.put(key, value);
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

package org.drama.core;

import static org.drama.text.MessageText.format;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.drama.event.Event;
import org.drama.event.EventResult;
import org.drama.event.EventResultEntity;
import org.drama.event.EventResultIndex;
import org.drama.event.EventResultValue;

/**
 * 事件结果
 */
public class DramaEventResult implements EventResult {
    private static final long serialVersionUID = 4555755009367480736L;
    private static final String INDEX_KEY = "KEY[{0}-{1}]";
    private final static Map<String, EventResultValue> RESULT_MAP = new ConcurrentHashMap<>();
    private final String identity = UUID.randomUUID().toString().toUpperCase();

    /**
     * 添加事件结果
     */
    @Override
    public void addResult(EventResultIndex index, EventResultValue value) {
        RESULT_MAP.put(getKey(index), value);
    }

    /**
     * 添加事件结果
     *
     * @param uuid   唯一id
     * @param event  关联事件
     * @param entity 事件结果实体
     * @param out    是否输出
     */
    @Override
    public void addResult(String uuid, Event event, EventResultEntity entity, boolean out) {
        EventResultIndex index = new EventResultIndex(uuid, event);
        EventResultValue value = new EventResultValue(entity, out);
        addResult(index, value);
    }

    /**
     * i
     * 获取事件结果
     *
     * @param key
     */
    @Override
    public EventResultValue getResult(EventResultIndex key) {
        return RESULT_MAP.get(getKey(key));
    }

    /**
     * 获取事件结果副本
     */
    @Override
    public Collection<EventResultValue> allResults() {
        return RESULT_MAP.values();
    }

    @PreDestroy
    protected void destroy() {
        RESULT_MAP.keySet().forEach(key -> {
            if (StringUtils.contains(key, identity)) {
                RESULT_MAP.remove(key);
            }
        });
    }

    private String getKey(EventResultIndex index) {
        return format(INDEX_KEY, identity, index);
    }

	@Override
	public void close() throws Exception {
		destroy();
	}
}

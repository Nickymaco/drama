package org.drama.core;

import static org.drama.text.MessageText.format;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.drama.delegate.Delegator;
import org.drama.event.EventContext;

public class DramaEventContext implements EventContext {
    private static final long serialVersionUID = 7285410254463046346L;
    private static final Map<String, Object> CONTEXT = new ConcurrentHashMap<>();
    private static final String KEY_FORMAT = "{0}:{1}";
    private final String identity = UUID.randomUUID().toString();
    private Layer currentLayer;

    @Override
    public void addParameter(String key, Object value) {
        CONTEXT.put(getKey(key), value);
    }

    @Override
    public Object getParameter(String key) {
        return CONTEXT.get(getKey(key));
    }

    @Override
    public Layer getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public void setCurrentLayer(Layer currentLayer) {
        this.currentLayer = currentLayer;
    }

    private String getKey(Object key) {
        return format(KEY_FORMAT, identity, key);
    }

    @PreDestroy
    protected void destroy() {
        Delegator.forEach(CONTEXT.keySet(), (key, i) -> {
            if (StringUtils.contains(key, identity)) {
                CONTEXT.remove(getKey(key));
            }
            return false;
        });
    }

	@Override
	public void close() throws Exception {
		destroy();
	}
}

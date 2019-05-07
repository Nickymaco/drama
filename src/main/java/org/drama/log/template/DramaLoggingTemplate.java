package org.drama.log.template;

import static org.drama.delegate.Delegator.forEach;
import static org.drama.text.MessageText.LAYER_BROADCAST;
import static org.drama.text.MessageText.LAYER_HANDING;
import static org.drama.text.MessageText.REGISTERED_ELEMENT;
import static org.drama.text.MessageText.REGISTERED_EVENT;
import static org.drama.text.MessageText.REGISTERED_LAYER;
import static org.drama.text.MessageText.STAGE_DEAL;
import static org.drama.text.MessageText.STAGE_IS_RUNNING;
import static org.drama.text.MessageText.STAGE_RECEVIE;
import static org.drama.text.MessageText.format;
import static org.drama.text.Symbol.WHITESPACE;
import static org.drama.text.Symbol.EMPTY;

import org.apache.commons.lang3.ArrayUtils;
import org.drama.core.Stage;
import org.drama.event.Event;
import org.drama.log.Logging;

class DramaLoggingTemplate extends BasicLoggingTemplate implements StageLoggingTemplate, LayerLoggingTemplate {
    public DramaLoggingTemplate(Logging logging) {
        super(logging);
    }

    @Override
    public void recevieEvent(Event[] events) {
        if (ArrayUtils.isEmpty(events)) {
            return;
        }

        StringBuilder build = new StringBuilder();

        forEach(events, p -> {
        	int idx = p.getParam2();
        	Event event = p.getParam1();
        	build.append(format("{0}{1}", event.getName(), idx != 0 ? WHITESPACE : EMPTY));
        });

        getLogging().info(format(STAGE_RECEVIE, build));
    }

    @Override
    public void dealEvent(Event event) {
        getLogging().info(format(STAGE_DEAL, event));
    }

    @Override
    public void broadcast(String layerName, Event event) {
        String eventName = event.getClass().getSimpleName();
        getLogging().info(format(LAYER_BROADCAST, layerName, eventName));
    }

    @Override
    public void handingElement(String elementName) {
        getLogging().info(format(LAYER_HANDING, elementName));
    }

    @Override
    public void registeredEvent(Class<?>[] events) {
        if (ArrayUtils.isEmpty(events)) {
            return;
        }
        for (Class<?> event : events) {
            getLogging().info(format(REGISTERED_EVENT, event.getName()));
        }

    }

    @Override
    public void regeisteredElement(Class<?>[] elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return;
        }
        for (Class<?> elem : elements) {
            getLogging().info(format(REGISTERED_ELEMENT, elem.getName()));
        }
    }

    @Override
    public void regeisteredLayer(String[] layers) {
        if (ArrayUtils.isEmpty(layers)) {
            return;
        }
        for (String layerName : layers) {
            getLogging().info(format(REGISTERED_LAYER, layerName));
        }
    }

    @Override
    public void setup(Stage stage) {
        getLogging().info(format(STAGE_IS_RUNNING, stage));
    }
}

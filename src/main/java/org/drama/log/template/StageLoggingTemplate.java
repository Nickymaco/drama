package org.drama.log.template;

import org.apache.commons.lang3.ArrayUtils;
import org.drama.core.Stage;
import org.drama.event.Event;
import org.drama.log.Logging;

class StageLoggingTemplate extends BasicLoggingTemplate implements IStageLoggingTemplate, ILayerLoggingTemplate {
    public StageLoggingTemplate(Logging logging) {
        super(logging);
    }

    @Override
    public void recevieEvent(Event[] events) {
        if (events == null || events.length == 0) {
            return;
        }

        StringBuilder build = new StringBuilder();

        for (int i = 0; i < events.length; i++) {
            if (i != 0) {
                build.append(WHITESPACE);
            }
            build.append(events[i].getClass().getName());
        }

        getLogging().info(String.format(STAGE_RECEVIE, build));
    }

    @Override
    public void dealEvent(Event event) {
        getLogging().info(String.format(STAGE_DEAL, event.getClass().getName()));
    }

    @Override
    public void broadcast(String layerName, Event event) {
        String eventName = event.getClass().getSimpleName();
        getLogging().info(String.format(LAYER_BROADCAST, layerName, eventName));
    }

    @Override
    public void handingElement(String elementName) {
        getLogging().info(String.format(LAYER_HANDING, elementName));
    }

    @Override
    public void registeredEvent(Class<?>[] events) {
        if (ArrayUtils.isEmpty(events)) {
            return;
        }
        for (Class<?> event : events) {
            getLogging().info(String.format(REGISTERED_EVENT, event.getName()));
        }

    }

    @Override
    public void regeisteredElement(Class<?>[] elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return;
        }
        for (Class<?> elem : elements) {
            getLogging().info(String.format(REGISTERED_ELEMENT, elem.getName()));
        }
    }

    @Override
    public void regeisteredLayer(String[] layers) {
        if (ArrayUtils.isEmpty(layers)) {
            return;
        }
        for (String layerName : layers) {
            getLogging().info(String.format(REGISTERED_LAYER, layerName));
        }
    }

    @Override
    public void setup(Stage stage) {
        getLogging().info(String.format(STAGE_IS_RUNNING, stage.getClass().getName()));
    }
}

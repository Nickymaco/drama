package org.drama.log.template;

import org.apache.commons.lang3.ArrayUtils;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.core.Stage;
import org.drama.event.Event;
import org.drama.log.Logging;

class StageLoggingTemplate extends BasicLoggingTemplate implements IStageLoggingTemplate, ILayerLoggingTemplate {
	public StageLoggingTemplate(Logging logging) {
        super(logging);
    }

	@Override
	public void recevieEvent(Event[] events) {
		if(events == null || events.length == 0) {
			return;
		}
		
		StringBuilder build = new StringBuilder();
		
		for(int i=0; i< events.length; i++) {
			if(i != 0) {
				build.append(WHITESPACE);
			}
			build.append(events[i].getClass().getSimpleName());
		}
		
		getLogging().info(STAGE_RECEVIE, build);
	}

	@Override
	public void dealEvent(Event event) {
		getLogging().info(STAGE_DEAL, event.getClass().getSimpleName());
	}

	@Override
	public void broadcast(String layerName, Event event) {
		String eventName = event.getClass().getSimpleName();
		getLogging().info(LAYER_BROADCAST, layerName, eventName);
	}

	@Override
	public void handingElement(Layer layer, Element element) {
		String layerName = layer.getClass().getSimpleName();
		String elementName = element.getClass().getSimpleName();
		getLogging().info(LAYER_HANDING, layerName, elementName);
	}

	@Override
	public void registeredEvent(Class<?>[] events) {
		if(ArrayUtils.isEmpty(events)) {
			return;
		}
		for(Class<?> event : events) {
			getLogging().info(REGISTERED_EVENT, event.getName());
		}
		
	}

	@Override
	public void regeisteredElement(Class<?>[] elements) {
		if(ArrayUtils.isEmpty(elements)) {
			return;
		}
		for(Class<?> elem : elements) {
			getLogging().info(REGISTERED_ELEMENT, elem.getName());
		}
	}

	@Override
	public void regeisteredLayer(String[] layers) {
		if(ArrayUtils.isEmpty(layers)) {
			return;
		}
		for(String layerName : layers) {
			getLogging().info(REGISTERED_LAYER, layerName);
		}
	}

	@Override
	public void setup(Stage stage) {
		getLogging().info(STAGE_IS_RUNNING, stage.getClass().getName());
	}
}

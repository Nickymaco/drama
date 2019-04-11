package org.drama.log.template;

import org.drama.common.MessageTemplate;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.log.Logging;

class LayerLoggingTemplate extends BasicLoggingTemplate implements ILayerLoggingTemplate {
    public LayerLoggingTemplate(Logging logging) {
        super(logging);
    }

	@Override
	public void logBroadcast(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String eventName = event.getClass().getSimpleName();
		this.getLogging().info(MessageTemplate.inst().getLogLayerBroadcast(), PREFIX, layerName, eventName);
	}

	@Override
	public void logHandingElement(Layer layer, Element element) {
		String layerName = layer.getClass().getSimpleName();
		String elementName = element.getClass().getSimpleName();
		this.getLogging().info(MessageTemplate.inst().getLogLayerHanding(), PREFIX, layerName, elementName);
	}
}

package com.john.drama.log.template;

import com.john.drama.core.Element;
import com.john.drama.core.Layer;
import com.john.drama.event.Event;
import com.john.drama.log.Logging;

class LayerLoggingTemplate implements ILayerLoggingTemplate {
    private Logging logging;

    public LayerLoggingTemplate(Logging logging) {
        this.logging = logging;
    }

    @Override
    public void catchException(Exception e) {
    	logging.error(e, "Stage occurred exception");  
    }

    @Override
    public Logging getLogging() {
        return this.logging;
    }

	@Override
	public void logBroadcast(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String eventName = event.getClass().getSimpleName();
		this.logging.info("{} Layer<{}> broadcast event {}", PREFIX, layerName, eventName);
	}

	@Override
	public void logHandingElement(Layer layer, Element element) {
		String layerName = layer.getClass().getSimpleName();
		String elementName = element.getClass().getSimpleName();
		this.logging.info("{} Layer<{}> handing element {}", PREFIX, layerName, elementName);
	}
}
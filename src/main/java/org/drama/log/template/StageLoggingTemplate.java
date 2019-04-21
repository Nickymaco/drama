package org.drama.log.template;

import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.log.Logging;

class StageLoggingTemplate extends BasicLoggingTemplate implements IStageLoggingTemplate, ILayerLoggingTemplate {
    public StageLoggingTemplate(Logging logging) {
        super(logging);
    }

	@Override
	public void logRecevieEvent(Event[] events) {
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
		
		this.getLogging().info(StageRecevie, IStageLoggingTemplate.PREFIX, build);
	}

	@Override
	public void logDealEvent(Event event) {
		this.getLogging().info(StageDeal, IStageLoggingTemplate.PREFIX, event.getClass().getSimpleName());
	}

	@Override
	public void logBroadcast(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String eventName = event.getClass().getSimpleName();
		this.getLogging().info(LayerBroadcast, ILayerLoggingTemplate.PREFIX, layerName, eventName);
	}

	@Override
	public void logHandingElement(Layer layer, Element element) {
		String layerName = layer.getClass().getSimpleName();
		String elementName = element.getClass().getSimpleName();
		this.getLogging().info(LayerHanding, ILayerLoggingTemplate.PREFIX, layerName, elementName);
	}
}

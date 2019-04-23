package org.drama.log.template;

import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.log.LoggingInteractor;

public interface ILayerLoggingTemplate extends LoggingInteractor {
	static final String PREFIX="#### LAYER #### ->";

	/**
	 * 记录传播事件
	 * @param event
	 */
	void broadcast(String layerName, Event event);
	/**
	 * 记录元素handing
	 * @param element
	 */
	void handingElement(Layer layer, Element element);
}
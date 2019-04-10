package com.john.drama.log.template;

import com.john.drama.core.Element;
import com.john.drama.core.Layer;
import com.john.drama.event.Event;
import com.john.drama.log.LoggingInteractor;

public interface ILayerLoggingTemplate extends LoggingInteractor {
	static final String PREFIX="#### LAYER #### ->";

	/**
	 * 记录传播事件
	 * @param event
	 */
	void logBroadcast(Layer layer, Event event);
	/**
	 * 记录元素handing
	 * @param element
	 */
	void logHandingElement(Layer layer, Element element);
}
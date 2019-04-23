package org.drama.log.template;

import org.drama.event.Event;
import org.drama.log.LoggingInteractor;

public interface IStageLoggingTemplate extends LoggingInteractor {
	static final String PREFIX="#### STAGE #### ->";
	
	/**
	 * 收到事件
	 * @param events
	 */
	void recevieEvent(Event[] events);
	/**
	 * 处理事件
	 * @param event
	 */
	void dealEvent(Event event);
}

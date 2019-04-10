package com.john.drama.log.template;

import com.john.drama.event.Event;
import com.john.drama.log.LoggingInteractor;

public interface IStageLoggingTemplate extends LoggingInteractor {
	static final String PREFIX="#### STAGE #### ->";
	
	/**
	 * 收到事件
	 * @param events
	 */
	void logRecevieEvent(Event[] events);
	/**
	 * 处理事件
	 * @param event
	 */
	void logDealEvent(Event event);
}

package org.drama.log.template;

import org.drama.core.Stage;
import org.drama.event.Event;
import org.drama.log.LoggingInteractor;

public interface IStageLoggingTemplate extends LoggingInteractor {
	static final String PREFIX="[ STAGE ] ->";
	
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
	/**
	 * 已注册事件
	 * @param events
	 */
	void registeredEvent(Class<?>[] events);
	/**
	 * 已注册元素
	 * @param elements
	 */
	void regeisteredElement(Class<?>[] elements);
	/**
	 * 已注册逻辑处理层
	 * @param layers
	 */
	void regeisteredLayer(String[] layerNames);
	/**
	 * 启动标记
	 * @param stage
	 */
	void setup(Stage stage);
}

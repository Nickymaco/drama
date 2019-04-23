package org.drama.core;

import java.io.Serializable;

import org.drama.log.LoggingFactory;

public interface Configuration extends Serializable {
	/**
	 * 内核
	 * @return
	 */
	Kernel getKernel();
	/**
	 * 事件传播监听器
	 * @return
	 */
	BroadcastLisenter getBroadcastLisenter();
	/**
	 * 逻辑处理层工厂
	 * @param layerFactory
	 */
	LayerFactory getLayerFactory();
	/**
	 * 注册元素
	 * @param element
	 */
	RegisterElementFactory getRegisterElementFactory();
	/**
	 * 注册事件
	 * @param events
	 */
	RegisterEventFactory getRegisterEventFactory();
	/**
	 * 添加日志
	 * @param loggingFactory
	 */
	LoggingFactory getLoggingFactory();
}
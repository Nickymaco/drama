package org.drama.core;

import java.io.Serializable;

import org.drama.log.LoggingFactory;

public interface Configuration extends Serializable {
	/**
	 * 内核
	 *
	 */
	Kernel getKernel();
	/**
	 * 事件传播监听器
	 *
	 */
	BroadcastLisenter getBroadcastLisenter();
	/**
	 * 逻辑处理层工厂
	 */
	LayerFactory getLayerFactory();
	/**
	 * 注册元素
	 */
	RegisterElementFactory getRegisterElementFactory();
	/**
	 * 注册事件
	 */
	RegisterEventFactory getRegisterEventFactory();
	/**
	 * 添加日志
	 */
	LoggingFactory getLoggingFactory();

	/**
	 * 默认错误输出
	 * @return
	 */
	Render defaultErrorRender();
}
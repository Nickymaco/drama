package org.drama.core;

import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.vo.BiParameterValueObject;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Kernel {
	/**
	 * 注册事件元数据
	 * @param eventClzs
	 */
	boolean regeisterEvent(Set<Class<? extends Event>> eventClzs);
	/**
	 * 注册元素
	 * @param element
	 */
	Layer registerElement(Element element);	
	/**
	 * 通知事件句柄
	 * @param layer
	 * @param event
	 * @param onPreHanding 在每一个逻辑处理层开始广播前执行
	 * @param onCompleted 事件完成回调，如果 {@code true} 则退出当前逻辑层不再往下执行
	 */
	void notifyHandler(Layer layer, Event event, Consumer<LayerContainer> onPreHanding, Consumer<Element> onCompleted);
	/**
	 * 获取逻辑处理层
	 *
	 */
	ImmutableSet<Layer> getlayers();
	/**
	 * Layer 构造工厂
	 * @param generator
	 */
	void setLayerGenerator(Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> generator);
	/**
	 * Layer 构造工厂
	 */
	Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> getLayerGenerator();
}
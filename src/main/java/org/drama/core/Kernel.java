package org.drama.core;

import java.util.Set;
import java.util.function.Function;

import org.drama.annotation.LayerProperty;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.OccurredException;
import org.drama.vo.BiParameterValueObject;

public interface Kernel {
	/**
	 * 注册事件元数据
	 * @param eventClzs
	 */
	boolean regeisterEvent(Set<Class<? extends Event>> eventClzs);
	/**
	 * 注册元素
	 * @param element
	 * @throws OccurredException 
	 */
	Layer registerElement(Element element);	
	/**
	 * 通知事件句柄
	 * @param layer
	 * @param event
	 * @param onCompleted
	 */
	void notifyHandler(Layer layer, Event event, Function<Broken, Boolean> onCompleted);
	/**
	 * 获取逻辑处理层
	 * @return
	 */
	ImmutableSet<Layer> getlayers();
	/**
	 * Layer 生成器
	 * @param generator
	 */
	void addLayerGenerator(Function<BiParameterValueObject<Class<? extends Layer>, LayerProperty>, Layer> generator);
}
package org.drama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.drama.core.DramaLayer;
import org.drama.core.Layer;
import org.drama.core.LayerDescriptor;
import org.drama.event.Event;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ElementProperty {
	/**
	 * 优先级
	 * @return
	 */
	int priority() default 0;
	/**
	 * 元素要监听的时间
	 * 
	 * @return
	 */
	Class<? extends Event>[] events();

	/**
	 * 指定具体逻辑处理层，需要提供一个无参构造函数
	 * 
	 * @return
	 */
	Class<? extends Layer> layer() default DramaLayer.class;
	
	/**
	 * 逻辑处理层描述，如果{@code layer}里找到描述则不会用此属性覆盖
	 * @return
	 */
	LayerDescription layerDesc() default @LayerDescription(desc = LayerDescriptor.Default.class, target = Layer.DefaultName); 
}
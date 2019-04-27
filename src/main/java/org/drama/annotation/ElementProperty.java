package org.drama.annotation;

import org.drama.core.Layer;
import org.drama.core.LayerDescriptor;
import org.drama.event.Event;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ElementProperty {
    /**
     * 优先级
     */
    int priority() default 0;

    /**
     * 元素要监听的时间
     */
    Class<? extends Event>[] events();

    /**
     * 指定具体逻辑处理层，需要提供一个无参构造函数
     */
    Class<? extends Layer> layer() default Layer.Null.class;

    /**
     * 逻辑处理层描述，如果{@code layer}里找到描述则不会用此属性覆盖
     */
    LayerDescription layerDesc() default @LayerDescription(desc = LayerDescriptor.Default.class, target = Layer.DEFAULT_NAME);
}
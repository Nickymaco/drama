package org.drama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.drama.core.Layer;
import org.drama.core.LayerDescriptor;

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
     * 监听所有事件，当设置为{@code true}时，events则不生效
     *
     * @return {@code true} listen any event or {@code false} only in events property
     */
    boolean any() default false;

    /**
     * 元素要监听的事件
     */
    String[] events() default {};

    /**
     * 指定具体逻辑处理层，需要提供一个无参构造函数
     */
    Class<? extends Layer> layer() default Layer.Null.class;

    /**
     * 逻辑处理层描述，如果{@code layer}里找到描述则不会用此属性覆盖
     */
    LayerDescription layerDesc() default @LayerDescription(desc = LayerDescriptor.Default.class, target = Layer.DEFAULT_NAME);
}
package org.drama.annotation;

import org.drama.core.Layer;
import org.drama.event.Event;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Inherited
public @interface LayerProperty {
    /**
     * 逻辑层处理名称，建议与priority通过枚举设定
     */
    String name() default Layer.DEFAULT_NAME;

    /**
     * 逻辑处理层优先级，用户控制stage优先处理哪一个逻辑层，建议通过与name枚举设定
     */
    int priority() default Layer.DEFAULT_PRIORITY;

    /**
     * 逻辑处理层的唯一标示（UUID）
     */
    String uuid() default Layer.DEFAULT_UUID;

    /**
     * 是否禁用
     */
    boolean disabled() default false;

    /**
     * 排除广播事件
     */
    Class<? extends Event>[] excludeEvent() default {};
}

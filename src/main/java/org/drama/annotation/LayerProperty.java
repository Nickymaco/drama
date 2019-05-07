package org.drama.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.drama.core.LayerContants.DEFAULT_NAME;
import static org.drama.core.LayerContants.DEFAULT_PRIORITY;
import static org.drama.core.LayerContants.DEFAULT_UUID;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.drama.core.Layer;

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Inherited
public @interface LayerProperty {
    /**
     * 逻辑层处理名称，建议与priority通过枚举设定
     */
    String name() default DEFAULT_NAME;

    /**
     * 逻辑处理层优先级，用户控制stage优先处理哪一个逻辑层，建议通过与name枚举设定
     */
    int priority() default DEFAULT_PRIORITY;

    /**
     * 逻辑处理层的唯一标示（UUID）
     */
    String uuid() default DEFAULT_UUID;

    /**
     * 是否禁用
     */
    boolean disabled() default false;

    /**
     * 排除广播事件
     */
    String[] excludeEvent() default {};
}

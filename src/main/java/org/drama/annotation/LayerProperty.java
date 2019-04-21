package org.drama.annotation;


import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface LayerProperty {
	/**
	 * 逻辑层处理名称，建议与priority通过枚举设定
	 * @return
	 */
	String name();
	/**
	 * 逻辑处理层优先级，用户控制stage优先处理哪一个逻辑层，建议通过与name枚举设定
	 * @return
	 */
	int priority();
	/**
	 * 逻辑处理层的唯一标示（UUID）
	 * @return
	 */
	String uuid();
	/**
	 * 是否禁用
	 * @return
	 */
	boolean disabled() default false;
}

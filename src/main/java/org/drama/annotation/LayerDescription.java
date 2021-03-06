package org.drama.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import org.drama.core.LayerDescriptor;

@Documented
@Retention(RUNTIME)
public @interface LayerDescription {
    /**
     * 用枚举表述一个逻辑处理层
     */
    Class<? extends Enum<? extends LayerDescriptor>> desc();

    /**
     * 指导{@code desc}选用哪个枚举
     */
    String target();
}

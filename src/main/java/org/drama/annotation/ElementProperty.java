package org.drama.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.drama.core.Layer;
import org.drama.event.Event;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ElementProperty {
	Class<? extends Event>[] registerEvent() default {};
	Class<? extends Layer> registerLayer();
}

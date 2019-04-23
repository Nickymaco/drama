package org.drama.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import org.drama.core.LayerDescriptor;

@Documented
@Retention(RUNTIME)
public @interface LayerDescription {
	Class<? extends Enum<? extends LayerDescriptor>> desc(); 
	String target();
}

package org.drama.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface ResultEntityAlias {
    String value();
}

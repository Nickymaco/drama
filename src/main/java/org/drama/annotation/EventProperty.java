package org.drama.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventProperty {
    /**
     * Indicate which event name can be support by this event object
     *
     * @return event names
     */
    String[] aliasFor() default {};
}

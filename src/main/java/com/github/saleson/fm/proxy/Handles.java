package com.github.saleson.fm.proxy;

import java.lang.annotation.*;

/**
 * @author saleson
 * @date 2020-01-04 13:29
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Handles {

    Handle[] value() default {};
}

package com.github.saleson.fm.proxy;

import java.lang.annotation.*;


/**
 * @author saleson
 * @date 2020-01-06 14:42
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Return(handler = TestHandler.class)
public @interface MapReturn {

    String value();
}

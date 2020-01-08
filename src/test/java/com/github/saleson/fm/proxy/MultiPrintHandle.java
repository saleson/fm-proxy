package com.github.saleson.fm.proxy;

import java.lang.annotation.*;

/**
 * @author saleson
 * @date 2020-01-08 13:07
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Handle(handler = APrintHandler.class, order = 2)
@Handle(handler = BPrintHandler.class, order = 1)
public @interface MultiPrintHandle {

    String value() default "";

    int order() default 0;
}

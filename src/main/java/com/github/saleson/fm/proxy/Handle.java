package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.Handler;

import java.lang.annotation.*;

/**
 * @author saleson
 * @date 2020-01-03 17:03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(Handles.class)
public @interface Handle {

    Class<? extends Handler> handler();

    int order() default Integer.MAX_VALUE;
}

package com.github.saleson.fm.proxy;


import com.github.saleson.fm.proxy.handle.ReturnHandler;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

/**
 * @author saleson
 * @date 2020-01-03 17:03
 */
@Target(ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Return {

    Class<? extends ReturnHandler> handler();

}

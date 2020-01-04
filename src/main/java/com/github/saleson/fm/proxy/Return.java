package com.github.saleson.fm.proxy;


import com.github.saleson.fm.proxy.handle.ReturnHandler;

import java.lang.annotation.*;

/**
 * @author saleson
 * @date 2020-01-03 17:03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Return {

    Class<? extends ReturnHandler> handler();

}

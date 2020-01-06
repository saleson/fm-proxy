package com.github.saleson.fm.proxy;

import java.lang.annotation.*;

/**
 * @author saleson
 * @date 2020-01-06 13:44
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Handle(handler = TestHandler.class)
public @interface TestOnAnno {


    String value();

}

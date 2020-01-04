package com.github.saleson.fm.proxy;

import java.lang.annotation.Annotation;

/**
 * @author saleson
 * @date 2020-01-03 17:13
 */
public @interface OverType {

    Class<? extends Annotation>[] types() default {};

}

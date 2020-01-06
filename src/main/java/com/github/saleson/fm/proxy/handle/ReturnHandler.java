package com.github.saleson.fm.proxy.handle;

import java.lang.annotation.Annotation;

/**
 * @author saleson
 * @date 2020-01-03 17:00
 */
public interface ReturnHandler<A extends Annotation, T> extends IHandler {

    T apply(A annotation, HandleArg arg);

}

package com.github.saleson.fm.proxy.handle;

import java.lang.annotation.Annotation;

/**
 * @author saleson
 * @date 2020-01-03 16:59
 */
public interface Handler<A extends Annotation> extends IHandler {

    void handle(A annotation, HandleArg arg);

}

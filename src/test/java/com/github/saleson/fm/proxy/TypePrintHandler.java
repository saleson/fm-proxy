package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.HandleArg;
import com.github.saleson.fm.proxy.handle.Handler;

import java.lang.annotation.Annotation;

/**
 * @author saleson
 * @date 2020-01-04 13:41
 */
public class TypePrintHandler implements Handler {

    @Override
    public void handle(Annotation annotation, HandleArg arg) {
        System.out.println("=====print=====" + arg.getMethod().getDeclaringClass());
    }
}

package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.HandleArg;
import com.github.saleson.fm.proxy.handle.Handler;

import java.lang.annotation.Annotation;

/**
 * @author saleson
 * @date 2020-01-04 13:41
 */
public class PrintHandler implements Handler<Handle> {

    @Override
    public void handle(Handle anno, HandleArg arg) {
        System.out.println("=====print=====" + arg.getMethod());
    }
}

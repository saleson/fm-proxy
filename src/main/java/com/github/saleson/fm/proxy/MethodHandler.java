package com.github.saleson.fm.proxy;

/**
 * @author saleson
 * @date 2020-01-03 17:31
 */
public interface MethodHandler {

    Object invoke(Object[] args) throws Throwable;
}

package com.github.saleson.fm.proxy;

import java.lang.reflect.Method;

/**
 * @author saleson
 * @date 2020-01-03 17:49
 */
public interface MethodHandlerFactory {

    MethodHandler create(Class<?> cls, Method method, MethodProxyMetadata metadata);



    class Default implements MethodHandlerFactory{

        @Override
        public MethodHandler create(Class<?> cls, Method method, MethodProxyMetadata metadata) {
            return new HandleMethodHandler(method, metadata);
        }
    }
}

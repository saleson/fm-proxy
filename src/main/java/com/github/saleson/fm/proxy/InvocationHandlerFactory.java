package com.github.saleson.fm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-01-03 17:44
 */
public interface InvocationHandlerFactory {

    InvocationHandler create(Class<?> cls, Map<Method, MethodHandler> methodHandlers);


    public class Default implements InvocationHandlerFactory {

        @Override
        public InvocationHandler create(Class<?> cls, Map<Method, MethodHandler> methodHandlers) {
            return new ProxyInterfaceInvocationHandler(methodHandlers);
        }
    }

}

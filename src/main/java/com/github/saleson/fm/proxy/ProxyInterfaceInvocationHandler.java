package com.github.saleson.fm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-02 21:25
 */
public class ProxyInterfaceInvocationHandler implements InvocationHandler {


    private Map<Method, MethodHandler> methodHandlers;

    public ProxyInterfaceInvocationHandler(Map<Method, MethodHandler> methodHandlers) {
        this.methodHandlers = methodHandlers;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object
                        otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }

        MethodHandler methodHandler = methodHandlers.get(method);
        if(Objects.isNull(methodHandler)){
            return null;
        }
        return methodHandler.invoke(args);
    }
}

package com.github.saleson.fm.proxy;


import com.github.saleson.fm.proxy.commons.Util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-01-03 16:58
 */
public class ProxyBuilder {

    private InvocationHandlerFactory invocationHandlerFactory;
    private MethodHandlerFactory methodHandlerFactory;
    private ProxyContract proxyContract;
    private MethodKeyGenerator methodKeyGenerator;


    public ProxyBuilder() {
        this(new InvocationHandlerFactory.Default(),
                new MethodHandlerFactory.Default(),
                new SimpleProxyContract(),
                new MethodKeyGenerator.Default());
    }


    public ProxyBuilder(InvocationHandlerFactory invocationHandlerFactory,
                        MethodHandlerFactory methodHandlerFactory,
                        ProxyContract proxyContract,
                        MethodKeyGenerator methodKeyGenerator) {
        this.invocationHandlerFactory = invocationHandlerFactory;
        this.methodHandlerFactory = methodHandlerFactory;
        this.proxyContract = proxyContract;
        this.methodKeyGenerator = methodKeyGenerator;
    }

    public <T> T newInstance(Class<T> cls) {
        List<MethodProxyMetadata> methodProxyMetadatas = proxyContract.parseAndValidatateProxyMetadata(cls);
        Map<String, MethodProxyMetadata> metadataMap = methodProxyMetadatas.stream()
                .collect(Collectors.toMap(MethodProxyMetadata::getMethodKey, m -> m));
        Map<Method, MethodHandler> methodHandlers = new HashMap<>();
        List<DefaultMethodHandler> defaultMethodHandlers = new ArrayList<>();

        for (Method method : cls.getMethods()) {
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
            if (Util.isDefault(method)) {
                defaultMethodHandlers.add(new DefaultMethodHandler(method));
            } else {
                MethodProxyMetadata methodProxyMetadata = metadataMap.get(methodKeyGenerator.generateKey(cls, method));
                methodHandlers.put(method, methodHandlerFactory.create(cls, method, methodProxyMetadata));
            }
        }

        InvocationHandler invocationHandler = invocationHandlerFactory.create(cls, methodHandlers);
        T bean = (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, invocationHandler);
        for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
            defaultMethodHandler.bindTo(bean);
        }
        return bean;
    }


}

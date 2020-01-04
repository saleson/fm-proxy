package com.github.saleson.fm.proxy;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-01-03 19:29
 */
public class SimpleProxyContract extends ProxyContract.BaseProxyContract {

    private MethodKeyGenerator methodKeyGenerator;


    private Map<Class, Object> instances = new ConcurrentHashMap<>();

    public SimpleProxyContract() {
        this(new MethodKeyGenerator.Default());
    }

    public SimpleProxyContract(MethodKeyGenerator methodKeyGenerator) {
        this.methodKeyGenerator = methodKeyGenerator;
    }

    @Override
    protected MethodKeyGenerator getMethodKeyGenerator() {
        return methodKeyGenerator;
    }

    @Override
    protected <T> T getInstance(Class<T> cls) throws IllegalAccessException, InstantiationException {
        Object bean = instances.get(cls);
        if (Objects.isNull(bean)) {
            bean = cls.newInstance();
            instances.put(cls, bean);
        }
        return (T)bean;
    }


}

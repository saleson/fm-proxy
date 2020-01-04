package com.github.saleson.fm.proxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author saleson
 * @date 2020-01-03 20:13
 */
public class ProxyTest {

    @Test
    public void testProxyHandler(){
        TestInterface test = new ProxyBuilder().newInstance(TestInterface.class);
        String a = test.a(10);
        System.out.println(a);
    }

}

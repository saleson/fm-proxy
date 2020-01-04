package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.commons.Util;

import java.lang.reflect.Method;

/**
 * @author saleson
 * @date 2020-01-03 17:58
 */
public interface MethodKeyGenerator {


    String generateKey(Class targetType, Method method);


    class Default implements MethodKeyGenerator{

        @Override
        public String generateKey(Class targetType, Method method) {
            return Util.generateMethodKey(targetType, method);
        }
    }
}

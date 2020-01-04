package com.github.saleson.fm.proxy.handle;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author saleson
 * @date 2020-01-03 17:01
 */
@Data
public class HandleArg {

    private HandleContext handleCxt = new HandleContext();

    private Method method;

    private Object[] args;

}

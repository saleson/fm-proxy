package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.HandleArg;
import com.github.saleson.fm.proxy.handle.ReturnHandler;

import java.util.Map;

/**
 * @author saleson
 * @date 2020-01-06 19:47
 */
public class TReturnHandler implements ReturnHandler<Return, String> {
    @Override
    public String apply(Return annotation, HandleArg arg) {
        System.out.println("return value:" + annotation.handler() + "... arg:" + arg);
        return "b";
    }
}

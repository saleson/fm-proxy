package com.github.saleson.fm.proxy;


import com.github.saleson.fm.proxy.handle.HandleArg;
import com.github.saleson.fm.proxy.handle.Handler;
import com.github.saleson.fm.proxy.handle.ReturnHandler;

/**
 * @author saleson
 * @date 2020-01-03 19:49
 */
public class TestHandler implements ReturnHandler<MapReturn, String>, Handler {


    @Override
    public void handle(HandleArg arg) {
        System.out.println("handle ... arg:" + arg);
    }

    @Override
    public String apply(MapReturn annotation, HandleArg arg) {
        System.out.println("return value:" + annotation.value() + "... arg:" + arg);
        return "a";
    }
}

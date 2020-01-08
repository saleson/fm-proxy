package com.github.saleson.fm.proxy.handle;

import com.github.saleson.fm.proxy.Handle;

/**
 * @author saleson
 * @date 2020-01-08 16:10
 */
public interface HHandler extends Handler<Handle> {


    default void handle(Handle annotation, HandleArg arg){
        handle(arg);
    }


    void handle(HandleArg arg);
}

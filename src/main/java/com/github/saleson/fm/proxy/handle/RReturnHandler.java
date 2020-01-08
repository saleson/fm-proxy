package com.github.saleson.fm.proxy.handle;

import com.github.saleson.fm.proxy.Return;


/**
 * @author saleson
 * @date 2020-01-03 17:00
 */
public interface RReturnHandler<T> extends ReturnHandler<Return, T> {

    default T apply(Return annotation, HandleArg arg){
        return apply(arg);
    }


    T apply(HandleArg arg);

}

package com.github.saleson.fm.proxy.handle;

/**
 * @author saleson
 * @date 2020-01-03 17:00
 */
public interface ReturnHandler<T> {

    T apply(HandleArg arg);

}

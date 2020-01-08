package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.HandleArg;
import com.github.saleson.fm.proxy.handle.Handler;

/**
 * @author saleson
 * @date 2020-01-08 13:08
 */
public class BPrintHandler implements Handler<MultiPrintHandle> {
    @Override
    public void handle(MultiPrintHandle annotation, HandleArg arg) {
        System.out.println(this.getClass() + " === " + annotation.value());
    }
}

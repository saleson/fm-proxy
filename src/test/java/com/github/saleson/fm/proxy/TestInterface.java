package com.github.saleson.fm.proxy;

/**
 * @author saleson
 * @date 2020-01-02 21:26
 */
@Handle(handler = TypePrintHandler.class, order =10)
public interface TestInterface {

    @Handle(handler = TestHandler.class, order = 12)
    @Handle(handler = PrintHandler.class, order = 11)
    @MapReturn("value1111...")
    String a(int a);
}

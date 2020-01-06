package com.github.saleson.fm.proxy.exception;

/**
 * @author saleson
 * @date 2020-01-06 16:24
 */
public class DuplicateException extends RuntimeException{

    public DuplicateException(){
        super();
    }


    public DuplicateException(String msg){
        super(msg);
    }
}

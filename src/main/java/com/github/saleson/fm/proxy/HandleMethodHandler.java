package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.HandleArg;
import com.github.saleson.fm.proxy.handle.HandleContext;
import com.github.saleson.fm.proxy.handle.Handler;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-03 18:18
 */
@Getter
public class HandleMethodHandler implements MethodHandler {

    private Method method;
    private MethodProxyMetadata methodProxyMetadata;

    public HandleMethodHandler(Method method, MethodProxyMetadata methodProxyMetadata) {
        this.method = method;
        this.methodProxyMetadata = methodProxyMetadata;
    }

    @Override
    public Object invoke(Object[] args) throws Throwable {
        HandleContext handleContext = new HandleContext();
        HandleArg handleArg = createHandleArg(handleContext, args);
        return handle(handleArg);
    }


    protected HandleArg createHandleArg(HandleContext handleContext, Object[] args){
        HandleArg handleArg = new HandleArg();
        handleArg.setArgs(args);
        handleArg.setHandleCxt(handleContext);
        handleArg.setMethod(method);
        return handleArg;
    }


    protected Object handle(HandleArg handleArg){
        for (Handler handler : methodProxyMetadata.getHandlers()) {
            handler.handle(handleArg);
        }
        if(Objects.isNull(methodProxyMetadata.getReturnHandler())){
            return null;
        }

        return methodProxyMetadata.getReturnHandler().apply(handleArg);
    }

}

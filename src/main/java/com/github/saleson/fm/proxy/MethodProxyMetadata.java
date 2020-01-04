package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.Handler;
import com.github.saleson.fm.proxy.handle.ReturnHandler;
import lombok.Data;

import java.util.List;

/**
 * @author saleson
 * @date 2020-01-03 17:28
 */
@Data
public class MethodProxyMetadata {

    private String methodKey;

    private List<Handler> handlers;

    private ReturnHandler returnHandler;

}

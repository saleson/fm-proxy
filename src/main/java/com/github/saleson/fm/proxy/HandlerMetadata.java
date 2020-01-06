package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.IHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * @author saleson
 * @date 2020-01-06 12:50
 */
@Data
@AllArgsConstructor
@Builder
public class HandlerMetadata<A extends Annotation, H extends IHandler> {
    private H handler;
    private A annotation;
}

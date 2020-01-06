package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.handle.Handler;
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
public class HandleMetadata<A extends Annotation> {
    private Annotation annotation;
    private A handleAnnotation;
}

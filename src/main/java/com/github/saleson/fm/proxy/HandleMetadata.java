package com.github.saleson.fm.proxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-06 12:50
 */
@Data
@AllArgsConstructor
@Builder
public class HandleMetadata<A extends Annotation> {
    private int order;
    private Annotation annotation;
    private A handleAnnotation;
}

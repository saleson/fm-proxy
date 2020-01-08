package com.github.saleson.fm.proxy.commons;

import com.github.saleson.fm.proxy.Handle;
import com.github.saleson.fm.proxy.Handles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-01-06 15:16
 */
public class AnnotationUtils {


    public static <A extends Annotation> List<Annotation> getTaggingAnnotations(
            AnnotatedElement element, Class<A> taggingAnnoCls, boolean repeatable) {
        return Arrays.stream(org.springframework.core.annotation.AnnotationUtils.getAnnotations(element))
                .filter(anno -> {
                    if (repeatable) {
                        return !Objects.isNull(AnnotatedElementUtils.getMergedRepeatableAnnotations(anno.getClass(), taggingAnnoCls));
                    }
                    return !Objects.isNull(AnnotatedElementUtils.getMergedAnnotation(anno.getClass(), taggingAnnoCls));
                })
                .collect(Collectors.toList());
    }


    public static <A extends Annotation> List<AnnotationTaggingMetadata<A>> getAnnotationTaggingMetadatas
            (AnnotatedElement element, Class<A> taggingAnnoCls, boolean searchChild, boolean repeatable) {
        Annotation[] annotations = org.springframework.core.annotation.AnnotationUtils.getAnnotations(element);
        List<AnnotationTaggingMetadata<A>> annotationTaggingMetadatas = new ArrayList<>(annotations.length);
        for (Annotation annotation : annotations) {
            if (searchChild && Objects.equals(annotation.annotationType(), taggingAnnoCls)) {
                AnnotationTaggingMetadata annotationTaggingMetadata = AnnotationTaggingMetadata.builder()
                        .annotation(annotation)
                        .taggingAnnotation(annotation)
                        .build();
                annotationTaggingMetadatas.add(annotationTaggingMetadata);
            } else if (repeatable) {
                annotationTaggingMetadatas.addAll(getRepeatableTaggingAnnotationMetadatas(annotation, taggingAnnoCls));
            } else {
                A taggingAnno = AnnotatedElementUtils.getMergedAnnotation(annotation.annotationType(), taggingAnnoCls);
                if (Objects.isNull(taggingAnno)) {
                    continue;
                }
                AnnotationTaggingMetadata annotationTaggingMetadata = AnnotationTaggingMetadata.builder()
                        .annotation(annotation)
                        .taggingAnnotation(taggingAnno)
                        .build();
                annotationTaggingMetadatas.add(annotationTaggingMetadata);
            }
        }
        return annotationTaggingMetadatas;
    }


    public static <A extends Annotation> List<AnnotationTaggingMetadata<A>> getRepeatableTaggingAnnotationMetadatas(
            Annotation annotation, Class<A> taggingAnnoCls) {
        return createAnnotationTaggingMetadatas(annotation,
                AnnotatedElementUtils.getMergedRepeatableAnnotations(annotation.annotationType(), taggingAnnoCls));
//        return AnnotatedElementUtils.getMergedRepeatableAnnotations(annotation.annotationType(), taggingAnnoCls)
//                .stream()
//                .map(taggingAnno -> {
//                    return AnnotationTaggingMetadata.<A>builder()
//                            .annotation(annotation)
//                            .taggingAnnotation(taggingAnno)
//                            .build();
//                })
//                .collect(Collectors.toList());
    }

    private static <A extends Annotation> List<AnnotationTaggingMetadata<A>> createAnnotationTaggingMetadatas(
            Annotation annotation, Collection<A> taggingAnnos) {
        return taggingAnnos.stream().map(taggingAnno -> {
            return AnnotationTaggingMetadata.<A>builder()
                    .annotation(annotation)
                    .taggingAnnotation(taggingAnno)
                    .build();
        }).collect(Collectors.toList());
    }


    private static <A extends Annotation> List<AnnotationTaggingMetadata<A>> createAnnotationTaggingMetadatas(Collection<A> taggingAnnos) {
        return taggingAnnos.stream().map(taggingAnno -> {
            return AnnotationTaggingMetadata.<A>builder()
                    .annotation(taggingAnno)
                    .taggingAnnotation(taggingAnno)
                    .build();
        }).collect(Collectors.toList());
    }


    @Data
    @AllArgsConstructor
    @Builder
    public static class AnnotationTaggingMetadata<A extends Annotation> {
        private A taggingAnnotation;
        private Annotation annotation;

    }
}

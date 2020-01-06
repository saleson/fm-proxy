package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.commons.AnnotationUtils;
import com.github.saleson.fm.proxy.commons.Helper;
import com.github.saleson.fm.proxy.commons.Util;
import com.github.saleson.fm.proxy.exception.DuplicateException;
import com.github.saleson.fm.proxy.handle.HandleGroup;
import com.github.saleson.fm.proxy.handle.Handler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-01-03 17:28
 */
public interface ProxyContract {

    List<MethodProxyMetadata> parseAndValidatateProxyMetadata(Class<?> targetType);


    @Slf4j
    public abstract class BaseProxyContract implements ProxyContract {

        @Override
        public List<MethodProxyMetadata> parseAndValidatateProxyMetadata(Class<?> targetType) {
            Map<String, MethodProxyMetadata> result = new LinkedHashMap<String, MethodProxyMetadata>();
            for (Method method : targetType.getMethods()) {
                if (method.getDeclaringClass() == Object.class ||
                        (method.getModifiers() & Modifier.STATIC) != 0 ||
                        Util.isDefault(method)) {
                    continue;
                }
                MethodProxyMetadata metadata = parseAndValidateMetadata(targetType, method);
                Util.checkState(!result.containsKey(metadata.getMethodKey()), "Overrides unsupported: %s",
                        metadata.getMethodKey());
                result.put(metadata.getMethodKey(), metadata);
            }
            return new ArrayList<>(result.values());
        }


        protected MethodProxyMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
            MethodProxyMetadata metadata = new MethodProxyMetadata();
            metadata.setMethodKey(getMethodKeyGenerator().generateKey(targetType, method));
            HandleGroup handleGroup = targetType.getInterfaces().length == 1 ?
                    getHandleGroupOnClass(targetType.getInterfaces()[0]) : getHandleGroupOnClass(targetType);

            HandleMetadata returnHandleMetadata = getReturnHandleMetadataOnAnnotatedElement(method);
            if (!Objects.isNull(returnHandleMetadata)) {
                metadata.setReturnHandlerMetadata(toReturnHandlerMetadata(returnHandleMetadata));
            }

            Collection<Handle> methodHandles = getHandlesOnAnnotatedElement(method);
            List<Handle> handles = new ArrayList<>(methodHandles);

            OverType overType = AnnotatedElementUtils.findMergedAnnotation(method, OverType.class);
            if ((Objects.isNull(overType) || !ArrayUtils.contains(overType.types(), Handle.class))
                    && !Objects.isNull(handleGroup.getHandles())) {
                handles.addAll(handleGroup.getHandles());
            }

            handles = Helper.sortHandles(handles);
            List<HandlerMetadata<Annotation, Handler>> handlerMetadatas = handles.stream()
                    .map(handle -> {
                        return HandlerMetadata.<Annotation, Handler>builder()
                                .annotation(handle)
                                .handler(getHandlerInstance(handle.handler()))
                                .build();
                    })
                    .collect(Collectors.toList());

            metadata.setHandlerMetadatas(handlerMetadatas);

            if ((Objects.isNull(overType) || !ArrayUtils.contains(overType.types(), Return.class))
                    && Objects.isNull(metadata.getReturnHandlerMetadata()) && !Objects.isNull(handleGroup.getReturnHandle())) {
                metadata.setReturnHandlerMetadata(toReturnHandlerMetadata(handleGroup.getReturnHandle()));
            }
            return metadata;
        }


        protected HandleGroup getHandleGroupOnClass(Class<?> cls) {
            HandleGroup handleGroup = new HandleGroup();

            HandleMetadata<Return> returnHandleMd = getReturnHandleMetadataOnAnnotatedElement(cls);
            if (!Objects.isNull(returnHandleMd)) {
                handleGroup.setReturnHandle(returnHandleMd);
            }
            handleGroup.setHandles(getHandlesOnAnnotatedElement(cls));
            return handleGroup;
        }


        protected Collection<Handle> getHandlesOnAnnotatedElement(AnnotatedElement element) {
            return AnnotatedElementUtils.findMergedRepeatableAnnotations(element, Handle.class);
        }


        protected HandleMetadata<Return> getReturnHandleMetadataOnAnnotatedElement(AnnotatedElement element) {
            List<AnnotationUtils.AnnotationTaggingMetadata<Return>> annotationTaggingMetadatas =
                    AnnotationUtils.getAnnotationTaggingMetadatas(element, Return.class, false);
            if(annotationTaggingMetadatas.size()>1){
                throw new DuplicateException("Duplicate Return Handler");
            }
            if(annotationTaggingMetadatas.size()==0){
                return null;
            }
            return annotationTaggingMetadatas.stream().findFirst().map(atm ->{
                return HandleMetadata.<Return>builder()
                        .annotation(atm.getAnnotation())
                        .handleAnnotation(atm.getTaggingAnnotation())
                        .build();
            }).get();
        }

        protected HandlerMetadata toReturnHandlerMetadata(HandleMetadata<Return> handleMetadata){
            return HandlerMetadata.builder().annotation(handleMetadata.getAnnotation())
                    .handler(getHandlerInstance(handleMetadata.getHandleAnnotation().handler()))
                    .build();
        }

        protected HandlerMetadata toHandlerMetadata(HandleMetadata<Handle> handleMetadata){
            return HandlerMetadata.builder().annotation(handleMetadata.getAnnotation())
                    .handler(getHandlerInstance(handleMetadata.getHandleAnnotation().handler()))
                    .build();
        }

        protected abstract MethodKeyGenerator getMethodKeyGenerator();

        protected <T> T getHandlerInstance(Class<T> cls) {
            try {
                return getInstance(cls);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        protected abstract <T> T getInstance(Class<T> cls) throws Exception;

    }

}

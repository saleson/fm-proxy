package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.commons.AnnotationUtils;
import com.github.saleson.fm.proxy.commons.Helper;
import com.github.saleson.fm.proxy.commons.Util;
import com.github.saleson.fm.proxy.exception.DuplicateException;
import com.github.saleson.fm.proxy.handle.HandleGroup;
import com.github.saleson.fm.proxy.handle.Handler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
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

            Collection<HandleMetadata<Handle>> methodHandles = getHandleMetadatasOnAnnotatedElement(method);
            List<HandleMetadata<Handle>> handleMetadatas = new ArrayList<>(methodHandles);

            OverType overType = AnnotatedElementUtils.findMergedAnnotation(method, OverType.class);
            if ((Objects.isNull(overType) || !ArrayUtils.contains(overType.types(), Handle.class))
                    && !Objects.isNull(handleGroup.getHandles())) {
                handleMetadatas.addAll(handleGroup.getHandles());
            }

            handleMetadatas = Helper.sortHandleMetadatas(handleMetadatas);
            List<HandlerMetadata<Annotation, Handler>> handlerMetadatas = handleMetadatas.stream()
                    .map(hm -> {
                        return HandlerMetadata.<Annotation, Handler>builder()
                                .annotation(hm.getAnnotation())
                                .handler(getHandlerInstance(hm.getHandleAnnotation().handler()))
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
            handleGroup.setHandles(getHandleMetadatasOnAnnotatedElement(cls));
            return handleGroup;
        }


        protected Collection<Handle> getHandlesOnAnnotatedElement(AnnotatedElement element) {
            return AnnotatedElementUtils.findMergedRepeatableAnnotations(element, Handle.class);
        }

        protected List<HandleMetadata<Handle>> getHandleMetadatasOnAnnotatedElement(AnnotatedElement element) {
            List<AnnotationUtils.AnnotationTaggingMetadata<Handle>> annotationTaggingMetadatas =
                    AnnotationUtils.getAnnotationTaggingMetadatas(element, Handle.class, true,true);

            List<HandleMetadata<Handle>> handleMetadataList = annotationTaggingMetadatas.stream().map(annoTaggingMetadata->{
                int order = annoTaggingMetadata.getTaggingAnnotation().order();
                if(!Objects.equals(annoTaggingMetadata.getAnnotation(), annoTaggingMetadata.getTaggingAnnotation())){
                    Map<String, Object> annoAttrs =
                            org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes(annoTaggingMetadata.getAnnotation());
                    int annoOrder = (Integer)annoAttrs.getOrDefault("order", 0);
                    order += annoOrder;
                }

                return HandleMetadata.<Handle>builder()
                        .annotation(annoTaggingMetadata.getAnnotation())
                        .handleAnnotation(annoTaggingMetadata.getTaggingAnnotation())
                        .order(order)
                        .build();

            }).collect(Collectors.toList());

            return ListUtils.union(handleMetadataList, getHandleMetadatasOnAnnotatedElementByHandles(element));
        }

        private List<HandleMetadata<Handle>> getHandleMetadatasOnAnnotatedElementByHandles(AnnotatedElement element){
            Handles handlesAnno = element.getAnnotation(Handles.class);
            if(Objects.isNull(handlesAnno)){
                return ListUtils.EMPTY_LIST;
            }

            return Arrays.stream(handlesAnno.value()).map(handle -> {
                return HandleMetadata.<Handle>builder()
                        .annotation(handle)
                        .handleAnnotation(handle)
                        .order(handle.order())
                        .build();
            }).collect(Collectors.toList());
        }



        protected HandleMetadata<Return> getReturnHandleMetadataOnAnnotatedElement(AnnotatedElement element) {
            List<AnnotationUtils.AnnotationTaggingMetadata<Return>> annotationTaggingMetadatas =
                    AnnotationUtils.getAnnotationTaggingMetadatas(element, Return.class, true,false);
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

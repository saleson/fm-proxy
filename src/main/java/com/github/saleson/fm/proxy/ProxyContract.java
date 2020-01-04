package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.commons.Helper;
import com.github.saleson.fm.proxy.commons.Util;
import com.github.saleson.fm.proxy.handle.HandleGroup;
import com.github.saleson.fm.proxy.handle.Handler;
import com.github.saleson.fm.proxy.handle.ReturnHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

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

            ReturnHandler returnHandler = getReturnHandlerOnAnnotatedElement(method);
            if (!Objects.isNull(returnHandler)) {
                metadata.setReturnHandler(returnHandler);
            }

            Collection<Handle> methodHandles = getHandlesOnAnnotatedElement(method);
            List<Handle> handles = new ArrayList<>(methodHandles);

            OverType overType = AnnotatedElementUtils.findMergedAnnotation(method, OverType.class);
            if ((Objects.isNull(overType) || !ArrayUtils.contains(overType.types(), Handle.class))
                    && !Objects.isNull(handleGroup.getHandles())) {
                handles.addAll(handleGroup.getHandles());
            }

            handles = Helper.sortHandles(handles);
            List<Handler> handlers = handles.stream()
                    .map(handle -> getHandlerInstance(handle.handler()))
                    .collect(Collectors.toList());

            metadata.setHandlers(handlers);

            if ((Objects.isNull(overType) || !ArrayUtils.contains(overType.types(), Return.class))
                    && Objects.isNull(metadata.getReturnHandler()) && !Objects.isNull(handleGroup.getReturnHandle())) {
                metadata.setReturnHandler(getHandlerInstance(handleGroup.getReturnHandle().handler()));
            }
            return metadata;
        }


        protected HandleGroup getHandleGroupOnClass(Class<?> cls) {
            HandleGroup handleGroup = new HandleGroup();

            Return returnHandle = getReturnHandleOnAnnotatedElement(cls);
            if (!Objects.isNull(returnHandle)) {
                handleGroup.setReturnHandle(returnHandle);
            }
            handleGroup.setHandles(getHandlesOnAnnotatedElement(cls));

//            ReturnHandler returnHandler = getReturnHandlerOnAnnotatedElement(cls);
//            if (!Objects.isNull(returnHandler)) {
//                handleGroup.setReturnHandler(returnHandler);
//            }
//            handleGroup.setHandlers(getHandlerOnAnnotatedElement(cls));
            return handleGroup;
        }


        protected Return getReturnHandleOnAnnotatedElement(AnnotatedElement element) {
            return AnnotatedElementUtils.findMergedAnnotation(element, Return.class);
        }

        protected Collection<Handle> getHandlesOnAnnotatedElement(AnnotatedElement element) {
            return AnnotatedElementUtils.findMergedRepeatableAnnotations(element, Handle.class);
        }


        protected ReturnHandler getReturnHandlerOnAnnotatedElement(AnnotatedElement element) {
            Return classAnnotation = AnnotatedElementUtils.findMergedAnnotation(element, Return.class);
            if (classAnnotation != null) {
                return getHandlerInstance(classAnnotation.handler());
            }
            return null;
        }

        protected List<Handler> getHandlerOnAnnotatedElement(AnnotatedElement element) {
            Set<Handle> classAnnotations = AnnotatedElementUtils.findMergedRepeatableAnnotations(element, Handle.class);
            List<Handle> handles = Helper.sortHandles(classAnnotations);
            List<Handler> handlers = new ArrayList<>(handles.size());
            for (Handle handle : handles) {
                handlers.add(getHandlerInstance(handle.handler()));
            }
            return handlers;
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

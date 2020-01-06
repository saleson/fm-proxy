package com.github.saleson.fm.proxy;

import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author saleson
 * @date 2020-01-06 13:47
 */
@TestOnAnno(value = "a")
public class OnAnnoTest {

    @Test
    public void test() {
        Handle handle = AnnotationUtils.getAnnotation(OnAnnoTest.class, Handle.class);
        System.out.println(handle);
    }
}

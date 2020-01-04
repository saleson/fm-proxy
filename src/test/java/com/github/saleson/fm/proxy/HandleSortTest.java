package com.github.saleson.fm.proxy;

import com.github.saleson.fm.proxy.commons.Helper;
import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.List;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-01-04 13:27
 */
//@Handle(handler = TestHandler.class, order = 2)
//@Handle(handler = TestHandler.class, order = 1)
//@Handle(handler = TestHandler.class, order = 7)
@Handle(handler = TestHandler.class, order = 3)
public class HandleSortTest {

    @Test
    public void testHandleSort(){
        Set<Handle> classAnnotations = AnnotatedElementUtils.findMergedRepeatableAnnotations(HandleSortTest.class, Handle.class);
        List<Handle> handles = Helper.sortHandles(classAnnotations);
        for (Handle handle : handles) {
            System.out.println(handle.order());
        }
    }

}

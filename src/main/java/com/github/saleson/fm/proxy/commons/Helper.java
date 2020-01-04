package com.github.saleson.fm.proxy.commons;

import com.github.saleson.fm.proxy.Handle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-01-04 13:20
 */
public class Helper {


    public static List<Handle> sortHandles(Collection<Handle> handles){
        return handles.stream().sorted(Comparator.comparing(Handle::order)).collect(Collectors.toList());
    }

}

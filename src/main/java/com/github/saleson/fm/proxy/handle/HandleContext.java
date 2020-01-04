package com.github.saleson.fm.proxy.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-01-03 17:02
 */
public class HandleContext {

    private Map<String, Object> attrs = new HashMap<>();


    public void putAttribute(String k, Object v){
        attrs.put(k, v);
    }

    public Object getAttribute(String k){
        return attrs.get(k);
    }

    public Object removeAttribute(String k){
        return attrs.remove(k);
    }

    public Set<String> getAttributeNames(){
        return attrs.keySet();
    }
}

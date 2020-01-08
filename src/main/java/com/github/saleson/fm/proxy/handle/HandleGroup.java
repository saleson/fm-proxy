package com.github.saleson.fm.proxy.handle;

import com.github.saleson.fm.proxy.Handle;
import com.github.saleson.fm.proxy.HandleMetadata;
import com.github.saleson.fm.proxy.Return;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-03 18:56
 */
@Data
public class HandleGroup {
    private Collection<HandleMetadata<Handle>> handles;
    private HandleMetadata<Return> returnHandle;


    public int handerSize() {
        return Objects.isNull(handles) ? 0 : handles.size();
    }
}

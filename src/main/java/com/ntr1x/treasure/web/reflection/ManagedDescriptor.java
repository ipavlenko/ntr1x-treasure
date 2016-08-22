package com.ntr1x.treasure.web.reflection;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.reflection.ResourceProperty.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManagedDescriptor {
    
    public final String name;
    public final String alias;
    public final Type create;
    public final Type update;
    
    public final Function<Resource, Object> get;
    public final BiConsumer<Resource, Object> set;
}

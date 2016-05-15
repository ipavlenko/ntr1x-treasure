package com.ntr1x.treasure.web.hk2;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import org.glassfish.jersey.ext.cdi1x.spi.Hk2CustomBoundTypesProvider;

public class EmptyHk2CustomBoundTypesProvider implements Hk2CustomBoundTypesProvider {

    @Override
    public Set<Type> getHk2Types() {
    	
    	return Collections.<Type>emptySet();
    }
}

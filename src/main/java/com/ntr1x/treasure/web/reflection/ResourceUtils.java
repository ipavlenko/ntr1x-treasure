package com.ntr1x.treasure.web.reflection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeanUtils;

import com.ntr1x.treasure.web.model.p0.Resource;

public class ResourceUtils {
    
    public static String alias(Resource parent, String locator, Resource resource) {
        
        return parent != null
            ? String.format("%s/%s/%d", parent.getAlias(), locator, resource.getId())
            : String.format("/%s/%d", locator, resource.getId())
        ;
    }
    
    public static <T extends Resource> T copyPropeties(
            ManagedDescriptor[] properties,
            T source,
            T target
    ) {
        for (ManagedDescriptor p : properties) {
            Object value = p.get.apply(source);
            p.set.accept(target, value);
        }
        
        return target;
    }
    
    public static <T extends Resource> ManagedDescriptor[] values(
            T managed,
            ResourceProperty.Type create,
            ResourceProperty.Type update
    ) {
        
        Collection<ManagedDescriptor> values = new ArrayList<>();
        
        Class<?> c = managed.getClass();
        while (c != null) {
            
            for (Field field : c.getDeclaredFields()) {
                
                ResourceProperty annotation = field.getAnnotation(ResourceProperty.class);
                if (annotation == null) annotation = ResourceProperty.Default.get();
                
                if (create != null && create != annotation.create()) continue;
                if (update != null && update != annotation.update()) continue;
                
                
                values.add(new ManagedDescriptor(
                    field.getName(),
                    annotation.locator().isEmpty() ? field.getName() : annotation.locator(),
                    annotation.create(),
                    annotation.update(),
                    (m) -> {
                        PropertyDescriptor p = BeanUtils.getPropertyDescriptor(m.getClass(), field.getName());
                        try {
                            return p.getReadMethod().invoke(m);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            throw new IllegalStateException(e);
                        }
                    },
                    (m, v) -> {
                        PropertyDescriptor p = BeanUtils.getPropertyDescriptor(m.getClass(), field.getName());
                        try {
                            p.getWriteMethod().invoke(m, v);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                ));
            }
            
            c = c.getSuperclass();
        }
        
        return values.toArray(new ManagedDescriptor[0]);
    }
}

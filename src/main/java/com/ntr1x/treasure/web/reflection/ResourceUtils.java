package com.ntr1x.treasure.web.reflection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeanUtils;

import com.ntr1x.treasure.web.model.Resource;

public class ResourceUtils {
    
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
    
//    public static <T extends Managed> void copyProperties(T source, T dest, ManagedDescriptor[] properties) {
//        
//        try {
//            
//            for (ManagedDescriptor p : properties) {
//                
//                PropertyDescriptor s = BeanUtils.getPropertyDescriptor(source.getClass(), p.name);
//                PropertyDescriptor d = BeanUtils.getPropertyDescriptor(dest.getClass(), p.name);
//                
//                Object value = s.getReadMethod().invoke(source);
//                d.getWriteMethod().invoke(dest, value);
//            }
//            
//        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            
//            throw new IllegalArgumentException(e);
//        }
//    }

//    public static <T extends Managed> void cascadeProperties(T source, T dest, ManagedDescriptor[] properties, Consumer<T> visitor) {
//        
//        try {
//            
//            for (ManagedDescriptor p : properties) {
//                
//                PropertyDescriptor s = BeanUtils.getPropertyDescriptor(source.getClass(), p.name);
//                PropertyDescriptor d = BeanUtils.getPropertyDescriptor(dest.getClass(), p.name);
//                
//                Object value = s.getReadMethod().invoke(source);
//                
//                if (value != null) {
//                    
//                    for (Object item : (Iterable<?>) value) {
//                        
//                        visitor.accept((T) item);
//                        
//                    }
//                }
//            }
//            
//        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            
//            throw new IllegalArgumentException(e);
//        }
//    }
}

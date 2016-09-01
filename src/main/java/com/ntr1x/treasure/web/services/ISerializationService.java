package com.ntr1x.treasure.web.services;

public interface ISerializationService {
    
    String stringify(Object object, Class<?>... context);
    <T> T parse(Class<T> clazz, String string, Class<?>... context);
}

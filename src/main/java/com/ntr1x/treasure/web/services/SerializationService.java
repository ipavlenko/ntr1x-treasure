package com.ntr1x.treasure.web.services;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.glassfish.jersey.message.filtering.spi.ObjectProvider;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.JerseyConfig.ServiceLocatorProvider;
import com.ntr1x.treasure.web.model.Resource.ResourceProperty;

@Service
public class SerializationService implements ISerializationService {
    
    @Inject
    private ServiceLocatorProvider locator;
    
    @Override
    public String stringify(Object object, Class<?>... context) {
        
        try {
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            Object graph = locator.get().getService(ObjectProvider.class).getFilteringObject(object.getClass(), false, ResourceProperty.Factory.get());
            
            Map<String, Object> jaxbProperties = new HashMap<String, Object>();
            jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
            jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
            
            JAXBContext jc = JAXBContext.newInstance(context, jaxbProperties);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.OBJECT_GRAPH, graph);
            
            marshaller.marshal(object, stream);
            
            return new String(stream.toByteArray());
            
        } catch (JAXBException e) {
            
            throw new IllegalArgumentException(e);
        }
        
    }

    @Override
    public <T> T parse(Class<T> clazz, String string, Class<?>... context) {
        
        try {
            
            StringReader reader = new StringReader(string);
            
            Object graph = locator.get().getService(ObjectProvider.class).getFilteringObject(clazz, false, ResourceProperty.Factory.get());
            
            Map<String, Object> jaxbProperties = new HashMap<String, Object>();
            jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
            jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
            
            JAXBContext jc = JAXBContext.newInstance(context, jaxbProperties);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setProperty(UnmarshallerProperties.OBJECT_GRAPH, graph);
            
            T result = unmarshaller.unmarshal(new StreamSource(reader), clazz).getValue();
            
            return result;
        
        } catch (JAXBException e) {
            
            throw new IllegalArgumentException(e);
        }
        
    }

}

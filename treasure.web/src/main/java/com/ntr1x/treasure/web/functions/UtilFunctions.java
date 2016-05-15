package com.ntr1x.treasure.web.functions;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import com.ntr1x.treasure.web.filters.HttpContext;

public class UtilFunctions {
	
	public static String json(Object object) throws IOException {
		
		try (StringWriter writer = new StringWriter()) {
			
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			 
			Marshaller marshaller = context.createMarshaller();
			
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
	        
	        marshaller.marshal(object, writer);
	        
	        return writer.toString();
	        
		} catch (JAXBException e) {
			
			throw new IOException(e);
		}
	}
	
	public static <T> T parseJson(String json, Class<T> clazz) {
		
		try {
			
			JAXBContext context = JAXBContext.newInstance(clazz);
			 
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
	        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
	        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
	        unmarshaller.setProperty(UnmarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
	        
	        return unmarshaller.unmarshal(new StreamSource(new StringReader(json)), clazz).getValue();
	        
		} catch (JAXBException e) {
			
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String host() {
		
		HttpServletRequest req = HttpContext.get().getRequest();
		String host = req.getHeader("X-Proxy-Host");
		if (host == null) {
			host = req.getHeader("X-Forwarded-Host");
		}
		if (host == null) {
			host = req.getServerName();
		}

		return host;
	}
}
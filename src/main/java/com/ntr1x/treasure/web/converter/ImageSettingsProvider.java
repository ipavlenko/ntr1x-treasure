package com.ntr1x.treasure.web.converter;


import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;

import lombok.Data;

@Provider
public class ImageSettingsProvider implements ParamConverterProvider {
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {

        if (type.equals(ImageSettingsConverter.class)) {
            return (ParamConverter<T>) new ImageSettingsConverter();
        }

        return null;
    }


    private class ImageSettingsConverter implements ParamConverter<ImageRectSettings> {

        @Override
        public ImageRectSettings fromString(String value) {

            Map<String, Object> jaxbProperties = new HashMap<String, Object>(2);
            jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
            jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
            JAXBContext jc = null;

            try {
                jc = JAXBContext.newInstance(new Class[] { ImageRectSettings.class }, jaxbProperties);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

            Unmarshaller unmarshaller = null;

            try {
                unmarshaller = jc.createUnmarshaller();
            } catch (JAXBException e) {
                e.printStackTrace();
            }

            ImageRectSettings result = new ImageRectSettings();

            try {
                result = (ImageRectSettings)unmarshaller.unmarshal(new StringReader(value));
            } catch (JAXBException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        public String toString(ImageRectSettings value) {

            return value.toString();
        }
    }

    public static ImageRectSettings convertToRectSettings(String settings) throws IllegalStateException {
        return convertToSettings(ImageRectSettings.class, ImageRectSettingsItem.class, settings);
    }

    public static ImageSquareSettings convertToSquareSettings(String settings) throws IllegalStateException {
        return convertToSettings(ImageSquareSettings.class, ImageSquareSettingsItem.class, settings);
    }

    public static ImageWidthSettings convertToWidthSettings(String settings) throws IllegalStateException {
        return convertToSettings(ImageWidthSettings.class, ImageWidthSettingsItem.class, settings);
    }

    private static <T, V> T convertToSettings(Class<T> settingsClass, Class<V> itemClass, String settings) throws IllegalStateException {

        if (settings != null) {

            try {
                Map<String, Object> jaxbProperties = new HashMap<>(2);
                jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
                jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);

                JAXBContext jc = JAXBContext.newInstance(new Class[]{
                        settingsClass,
                        itemClass
                }, jaxbProperties);

                StreamSource json = new StreamSource(new StringReader(settings));
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                return unmarshaller.unmarshal(json, settingsClass).getValue();
            } catch (JAXBException e) {
                throw new IllegalStateException(e);
            }
        }

        return null;
    }

    @Data
    @XmlRootElement
    public static class ImageRectSettings {
        @XmlElement
        public Collection<ImageRectSettingsItem> items;
    }

    @Data
    @XmlRootElement
    public static class ImageRectSettingsItem {
        public String  name;
        public Integer width;
        public Integer height;
    }

    @Data
    @XmlRootElement
    public static class ImageSquareSettings {
        @XmlElement
        public Collection<ImageSquareSettingsItem> items;
    }

    @Data
    @XmlRootElement
    public static class ImageSquareSettingsItem {
        public String  name;
        public Integer side;
    }


    @Data
    @XmlRootElement
    public static class ImageWidthSettings {
        @XmlElement
        public Collection<ImageWidthSettingsItem> items;
    }

    @Data
    @XmlRootElement
    public static class ImageWidthSettingsItem {
        public String  name;
        public Integer width;
    }

}

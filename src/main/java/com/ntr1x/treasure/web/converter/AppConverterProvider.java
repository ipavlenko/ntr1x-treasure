package com.ntr1x.treasure.web.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@Provider
public class AppConverterProvider implements ParamConverterProvider {

	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> type, Type genericType, Annotation[] annotations) {
		
		if (type.equals(LocalDate.class)) {
			return (ParamConverter<T>) new LocalDateConverter();
		} else if (type.equals(LocalTime.class)) {
			return (ParamConverter<T>) new LocalTimeConverter();
		} else if (type.equals(LocalDateTime.class)) {
			return (ParamConverter<T>) new LocalDateTimeConverter();
		}
		
		return null;
	}
	
	private static class LocalDateConverter extends XmlAdapter<String, LocalDate> implements ParamConverter<LocalDate> {
		
		@Override
		public LocalDate fromString(String value) {
			return value == null ? null : LocalDate.parse(value);
		}

		@Override
		public String toString(LocalDate value) {
			return value == null ? null : value.toString();
		}

		@Override
		public LocalDate unmarshal(String v) {
			return fromString(v);
		}

		@Override
		public String marshal(LocalDate v) {
			return toString(v);
		}
	}
	
	public static class LocalTimeConverter extends XmlAdapter<String, LocalTime> implements ParamConverter<LocalTime> {
		
		@Override
		public LocalTime fromString(String value) {
			return value == null ? null : LocalTime.parse(value);
		}

		@Override
		public String toString(LocalTime value) {
			return value == null ? null : value.toString();
		}

		@Override
		public LocalTime unmarshal(String v) {
			return fromString(v);
		}

		@Override
		public String marshal(LocalTime v) {
			return toString(v);
		}
	}
	
	public static class LocalDateTimeConverter extends XmlAdapter<String, LocalDateTime> implements ParamConverter<LocalDateTime> {
		
		@Override
		public LocalDateTime fromString(String value) {
			return value == null ? null : LocalDateTime. parse(value);
		}

		@Override
		public String toString(LocalDateTime value) {
			return value == null ? null : value.toString();
		}
		
		@Override
		public LocalDateTime unmarshal(String v) {
			return fromString(v);
		}

		@Override
		public String marshal(LocalDateTime v) {
			return toString(v);
		}
	}
}

package com.ntr1x.treasure.web.providers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import com.ntr1x.treasure.security.ISecurityService;
import com.ntr1x.treasure.security.SecurityService;

import lombok.Data;

@Provider
public class AppConverterProvider implements ParamConverterProvider {
	
	@PersistenceContext(unitName="treasure.core")
	private EntityManager em;
	
	@Inject
	private SecurityService security;
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> type, Type genericType, Annotation[] annotations) {
		
		if (type.equals(LocalDate.class)) {
			return (ParamConverter<T>) new LocalDateConverter();
		} else if (type.equals(LocalTime.class)) {
			return (ParamConverter<T>) new LocalTimeConverter();
		} else if (type.equals(LocalDateTime.class)) {
			return (ParamConverter<T>) new LocalDateTimeConverter();
		} else if (type.equals(ISecurityService.SecurityToken.class)) {
			return (ParamConverter<T>) new SecurityTokenConverter();
		}
		
		return null;
	}
	
	private static class LocalDateConverter implements ParamConverter<LocalDate> {
		
		@Override
		public LocalDate fromString(String value) {
			try {
				return LocalDate.parse(value);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public String toString(LocalDate value) {
			return value.toString();
		}
	}
	
	private static class LocalTimeConverter implements ParamConverter<LocalTime> {
		
		@Override
		public LocalTime fromString(String value) {
			try {
				return LocalTime.parse(value);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public String toString(LocalTime value) {
			return value.toString();
		}

	}
	
	private static class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {
		
		@Override
		public LocalDateTime fromString(String value) {
			try {
				return LocalDateTime.parse(value);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public String toString(LocalDateTime value) {
			return value.toString();
		}
	}
	
	@Data
	private class SecurityTokenConverter implements ParamConverter<ISecurityService.SecurityToken> {
		
		@Override
		public ISecurityService.SecurityToken fromString(String value) {
			
			return security.parseToken(value);
		}

		@Override
		public String toString(ISecurityService.SecurityToken value) {
			
			return security.toString(value);
		}
	}
}

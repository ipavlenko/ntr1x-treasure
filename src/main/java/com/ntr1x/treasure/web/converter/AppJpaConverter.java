package com.ntr1x.treasure.web.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public interface AppJpaConverter {
	
	@Converter(autoApply = true)
	public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

		@Override
		public Date convertToDatabaseColumn(LocalDate date) {
			return date == null ? null : Date.valueOf(date);
		}

		@Override
		public LocalDate convertToEntityAttribute(Date date) {
			return date == null ? null : date.toLocalDate();
		}
	}
	
	@Converter(autoApply = true)
	public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

		@Override
		public Timestamp convertToDatabaseColumn(LocalDateTime date) {
			return date == null ? null : Timestamp.valueOf(date);
		}

		@Override
		public LocalDateTime convertToEntityAttribute(Timestamp date) {
			return date == null ? null : date.toLocalDateTime();
		}
	}
}

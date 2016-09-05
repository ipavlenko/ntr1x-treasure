package com.ntr1x.treasure.web.model.converters;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime datetime) {
        return datetime != null ? Timestamp.valueOf(datetime) : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp value) {
        return value != null ? value.toLocalDateTime() : null;
    }
}
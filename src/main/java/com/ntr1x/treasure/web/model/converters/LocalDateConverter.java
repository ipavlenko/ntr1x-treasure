package com.ntr1x.treasure.web.model.converters;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate datetime) {
        return datetime != null ? Date.valueOf(datetime) : null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Date value) {
        return value != null ? value.toLocalDate() : null;
    }
}
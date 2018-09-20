package utilities.adapter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date>
{

    @Override
    public Date convertToDatabaseColumn(LocalDate LocalDate)
    {
        return (LocalDate == null ? null : Date.valueOf(LocalDate));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate)
    {
        return (sqlDate == null ? null : sqlDate.toLocalDate());
    }

}

package utilities.adapter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>
{

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime LocalDateTime)
    {
        return (LocalDateTime == null ? null : Timestamp.valueOf(LocalDateTime));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlDate)
    {
        return (sqlDate == null ? null : sqlDate.toLocalDateTime());
    }

}

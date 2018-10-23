package utilities.adapter;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time>
{

    @Override
    public Time convertToDatabaseColumn(LocalTime localTime)
    {
        return (localTime == null ? null : Time.valueOf(localTime));
    }

    @Override
    public LocalTime convertToEntityAttribute(Time sqlTime)
    {
        return (sqlTime == null ? null : sqlTime.toLocalTime());
    }

}

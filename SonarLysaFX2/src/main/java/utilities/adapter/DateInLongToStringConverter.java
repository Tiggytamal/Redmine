package utilities.adapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javafx.util.StringConverter;

/**
 * Permet de convertir une date au format Long en date afficahble "jj-MM-yyyy"
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class DateInLongToStringConverter extends StringConverter<Number>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String toString(Number object)
    {
        return Instant.ofEpochMilli(object.longValue()).atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }

    @Override
    public Number fromString(String string)
    {
        return LocalDate.parse(string).atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

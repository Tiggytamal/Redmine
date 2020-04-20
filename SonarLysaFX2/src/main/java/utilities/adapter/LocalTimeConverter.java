package utilities.adapter;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.Converter;

/**
 * Classe de gestion des {@link java.time.LocalTime} pour la persistance en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@Converter(autoApply = true)
public class LocalTimeConverter extends AbstractAttributeConverter<LocalTime, Time>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public LocalTimeConverter()
    {
        super(Time::toLocalTime, Time::valueOf);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package utilities.adapter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Converter;

/**
 * Classe de gestion des {@link java.time.LocalDateTime} pour la persistance en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter extends AbstractAttributeConverter<LocalDateTime, Timestamp>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public LocalDateTimeConverter()
    {
        super(Timestamp::toLocalDateTime, Timestamp::valueOf);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

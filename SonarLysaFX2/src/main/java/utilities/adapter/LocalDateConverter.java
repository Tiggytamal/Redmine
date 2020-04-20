package utilities.adapter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Converter;

/**
 * Classe de gestion des {@link java.time.LocalDate} pour la persistance en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@Converter(autoApply = true)
public class LocalDateConverter extends AbstractAttributeConverter<LocalDate, Date>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public LocalDateConverter()
    {
        super(Date::toLocalDate, Date::valueOf);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

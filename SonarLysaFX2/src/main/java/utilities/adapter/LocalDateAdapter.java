package utilities.adapter;

import java.time.LocalDate;

/**
 * Classe de gestion des {@code LocalDate} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 * 
 */
public class LocalDateAdapter extends AbstractXmlAdapter<String, LocalDate>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public LocalDateAdapter()
    {
        super(LocalDate::parse, LocalDate::toString);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

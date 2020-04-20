package utilities.adapter;

import java.time.LocalTime;

/**
 * Classe de gestion des {@link java.time.LocalTime} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class LocalTimeAdapter extends AbstractXmlAdapter<String, LocalTime>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public LocalTimeAdapter()
    {
        super(LocalTime::parse, LocalTime::toString);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

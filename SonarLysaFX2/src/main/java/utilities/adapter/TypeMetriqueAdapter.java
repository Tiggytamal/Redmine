package utilities.adapter;

import model.enums.Metrique;

/**
 * Classe de gestion des {@link model.enums.Metrique} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TypeMetriqueAdapter extends AbstractXmlAdapter<String, Metrique>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TypeMetriqueAdapter()
    {
        super(Metrique::from, Metrique::getValeur);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

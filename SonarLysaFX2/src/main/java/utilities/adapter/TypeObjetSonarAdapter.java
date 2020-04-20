package utilities.adapter;

import model.enums.TypeObjetSonar;

/**
 * Classe de gestion des {@link model.enums.TypeObjetSonar} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TypeObjetSonarAdapter extends AbstractXmlAdapter<String, TypeObjetSonar>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TypeObjetSonarAdapter()
    {
        super(TypeObjetSonar::from, TypeObjetSonar::getValeur);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

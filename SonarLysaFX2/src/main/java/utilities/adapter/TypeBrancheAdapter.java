package utilities.adapter;

import model.enums.TypeBranche;

/**
 * Classe de gestion des {@link model.enums.TypeBranche} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TypeBrancheAdapter extends AbstractXmlAdapter<String, TypeBranche>
{

    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TypeBrancheAdapter()
    {
        super(TypeBranche::from, TypeBranche::getValeur);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

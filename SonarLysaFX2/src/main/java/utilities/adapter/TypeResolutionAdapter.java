package utilities.adapter;

import model.enums.TypeResolution;

/**
 * * Classe de gestion des {@link model.enums.TypeResolution} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class TypeResolutionAdapter extends AbstractXmlAdapter<String, TypeResolution>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    public TypeResolutionAdapter()
    {
        super(TypeResolution::from, TypeResolution::toString);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

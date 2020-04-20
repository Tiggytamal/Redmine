package utilities.adapter;

import model.enums.InstanceSonar;

/**
 * Classe de gestion des {@link model.enums.InstanceSonar} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class InstanceSonarAdapter extends AbstractXmlAdapter<String, InstanceSonar>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    public InstanceSonarAdapter()
    {
        super(InstanceSonar::from, InstanceSonar::getValeur);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

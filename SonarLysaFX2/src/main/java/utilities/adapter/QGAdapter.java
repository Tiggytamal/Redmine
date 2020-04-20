package utilities.adapter;

import model.enums.QG;

/**
 * Classe de gestion des {@link model.enums.QG} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class QGAdapter extends AbstractXmlAdapter<String, QG>
{

    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public QGAdapter()
    {
        super(QG::from, QG::getValeur);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

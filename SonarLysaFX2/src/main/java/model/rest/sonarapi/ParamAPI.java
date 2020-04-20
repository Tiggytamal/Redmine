package model.rest.sonarapi;

import model.AbstractModele;

/**
 * Classe de modele représentant un paramètre à envoyer à un webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ParamAPI extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String clef;
    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamAPI(String clef, String valeur)
    {
        this.clef = clef;
        this.valeur = valeur;
    }

    ParamAPI()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getClef()
    {
        return getString(clef);
    }

    public void setClef(String clef)
    {
        this.clef = getString(clef);
    }

    public String getValeur()
    {
        return getString(valeur);
    }

    public void setValeur(String valeur)
    {
        this.valeur = getString(valeur);
    }
}

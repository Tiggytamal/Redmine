package model.sonarapi;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant un paramètre à envoyer à un webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class Parametre extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String clef;
    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    public Parametre(String clef, String valeur)
    {
        this.clef = clef;
        this.valeur = valeur;
    }

    public Parametre()
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
        this.clef = clef;
    }

    public String getValeur()
    {
        return getString(valeur);
    }

    public void setValeur(String valeur)
    {
        this.valeur = valeur;
    }
}

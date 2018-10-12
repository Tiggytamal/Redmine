package model.sonarapi;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de mod�le repr�sentant un param�tre � envoyer � un webservice Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
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
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
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

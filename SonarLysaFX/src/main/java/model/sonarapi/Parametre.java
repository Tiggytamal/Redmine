package model.sonarapi;

public class Parametre
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
        return clef;
    }

    public void setClef(String clef)
    {
        this.clef = clef;
    }

    public String getValeur()
    {
        return valeur;
    }

    public void setValeur(String valeur)
    {
        this.valeur = valeur;
    }
}

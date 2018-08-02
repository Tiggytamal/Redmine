package model;

/**
 * Classe de modèle pour les informations extraites des traitements à envoyer par mail.
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class InfoMail implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private String lot;
    private String infoSupp;

    /*---------- CONSTRUCTEURS ----------*/

    InfoMail() { }
    
    InfoMail(String lot, String infoSupp)
    {
        this.lot = lot;
        this.infoSupp = infoSupp;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public String getInfoSupp()
    {
        return getString(infoSupp);
    }

    public void setInfoSupp(String infoSupp)
    {
        this.infoSupp = infoSupp;
    }
}

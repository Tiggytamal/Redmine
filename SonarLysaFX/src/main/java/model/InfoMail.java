package model;

/**
 * Classe de mod�le pour les informations extraites des traitements � envoyer par mail.
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public class InfoMail
{
    /*---------- ATTRIBUTS ----------*/

    private String lot;
    private String infoSupp;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public InfoMail(String lot, String infoSupp)
    {
        super();
        this.lot = lot;
        this.infoSupp = infoSupp;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getLot()
    {
        return lot;
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }
    public String getInfoSupp()
    {
        return infoSupp;
    }
    public void setInfoSupp(String infoSupp)
    {
        this.infoSupp = infoSupp;
    }
}
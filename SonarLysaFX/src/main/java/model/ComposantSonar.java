package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ComposantSonar implements Modele
{
    /*---------- ATTRIBUTS ----------*/
    
    private String nom;
    private String lot;
    private String key;
    private String appli;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    ComposantSonar() {}
    
    /*---------- METHODES PUBLIQUES ----------*/  
    
    @XmlAttribute (name = "nom", required = true)
    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute (name = "lot", required = false)
    public String getLot()
    {
        return lot;
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    @XmlAttribute (name = "key", required = true)
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute (name = "appli", required = false)
    public String getAppli()
    {
        return appli;
    }

    public void setAppli(String appli)
    {
        this.appli = appli;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

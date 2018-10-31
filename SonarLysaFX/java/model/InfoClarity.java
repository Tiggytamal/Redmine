package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InfoClarity implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private boolean actif;
    private String codeClarity;
    private String libelleProjet;
    private String chefProjet;
    private String edition;
    private String direction;
    private String departement;
    private String service;

    /*---------- CONSTRUCTEURS ----------*/

    InfoClarity() {}

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "actif", required = false)
    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

    @XmlAttribute(name = "codeClarity", required = false)
    public String getCodeClarity()
    {
        return getString(codeClarity);
    }

    public void setCodeClarity(String codeClarity)
    {
        this.codeClarity = codeClarity;
    }

    @XmlAttribute(name = "libelleProjet", required = false)
    public String getLibelleProjet()
    {
        return getString(libelleProjet);
    }

    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = libelleProjet;
    }

    @XmlAttribute(name = "chefProjet", required = false)
    public String getChefProjet()
    {
        return getString(chefProjet);
    }

    public void setChefProjet(String chefProjet)
    {
        this.chefProjet = chefProjet;
    }

    @XmlAttribute(name = "edition", required = false)
    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    /**
     * @return the direction
     */
    @XmlAttribute(name = "direction", required = false)
    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    @XmlAttribute(name = "departement", required = false)
    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    @XmlAttribute(name = "service", required = false)
    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = service;
    }
}

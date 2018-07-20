package model;

import java.io.Serializable;

/**
 * Classe de modèle pour l'extraction des vulnérabilités CVE
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class Vulnerabilite implements Serializable, Modele
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String severite;
    private String composant;
    private String status;
    private String message;
    private String dateCreation;
    private String lot;
    private String clarity;
    private String appli;
    private String lib;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLib()
    {
        return getString(lib);
    }

    public void setLib(String lib)
    {
        this.lib = lib;
    }

    public String getAppli()
    {
        return getString(appli);
    }

    public void setAppli(String appli)
    {
        this.appli = appli;
    }

    public String getSeverite()
    {
        return getString(severite);
    }

    public void setSeverite(String severite)
    {
        this.severite = severite;
    }

    public String getComposant()
    {
        return getString(composant);
    }

    public void setComposant(String composant)
    {
        this.composant = composant;
    }

    public String getStatus()
    {
        return getString(status);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return getString(message);
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getDateCreation()
    {
        return getString(dateCreation);
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation = dateCreation;
    }

    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public String getClarity()
    {
        return getString(clarity);
    }

    public void setClarity(String clarity)
    {
        this.clarity = clarity;
    }
}
package model;

import java.io.Serializable;

/**
 * Classe de modèle pour l'extraction des vulnérabilités CVE
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class Vulnerabilite implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String Severite;
    private String composant;
    private String status;
    private String message;
    private String dateCreation;
    private String lot;
    private String Clarity;
    private String appli;
    private String lib;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLib()
    {
        return lib;
    }

    public void setLib(String lib)
    {
        this.lib = lib;
    }

    public String getAppli()
    {
        return appli;
    }

    public void setAppli(String appli)
    {
        this.appli = appli;
    }

    public String getSeverite()
    {
        return Severite;
    }

    public void setSeverite(String severite)
    {
        Severite = severite;
    }

    public String getComposant()
    {
        return composant;
    }

    public void setComposant(String composant)
    {
        this.composant = composant;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation = dateCreation;
    }

    public String getLot()
    {
        return lot;
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public String getClarity()
    {
        return Clarity;
    }

    public void setClarity(String clarity)
    {
        Clarity = clarity;
    }

}

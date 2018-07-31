package model;

import java.io.Serializable;

/**
 * Calsse de modèle du fichier Excel des responsables de services
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class RespService implements Modele, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String filiere;
    private String direction;
    private String service;
    private String departement;
    private String nom;

    /*---------- CONSTRUCTEURS ----------*/

    RespService() { }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getFiliere()
    {
        return getString(filiere);
    }

    public void setFiliere(String filiere)
    {
        this.filiere = filiere;
    }

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
}

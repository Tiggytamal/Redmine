package model;

import java.io.Serializable;

public class RespService implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String filiere;
    private String direction;
    private String service;
    private String departement;
    private String nom;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getFiliere()
    {
        return filiere;
    }

    public void setFiliere(String filiere)
    {
        this.filiere = filiere;
    }

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getDepartement()
    {
        return departement;
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
}

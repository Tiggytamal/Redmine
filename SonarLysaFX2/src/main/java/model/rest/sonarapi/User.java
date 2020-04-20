package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de modele représentant un utilisateur en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
@JsonIgnoreProperties({"avatar"})
public class User extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String login;
    private String nom;
    private String active;
    private String email;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "login")
    public String getLogin()
    {
        return getString(login);
    }

    public void setLogin(String login)
    {
        this.login = getString(login);
    }

    @XmlAttribute(name = "name")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String name)
    {
        this.nom = getString(name);
    }

    @XmlAttribute(name = "active")
    public String getActive()
    {
        return getString(active);
    }

    public void setActive(String active)
    {
        this.active = getString(active);
    }

    @XmlAttribute(name = "email")
    public String getEmail()
    {
        return getString(email);
    }

    public void setEmail(String email)
    {
        this.email = getString(email);
    }
}

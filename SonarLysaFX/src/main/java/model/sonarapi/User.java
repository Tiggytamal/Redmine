package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User
{
    /*---------- ATTRIBUTS ----------*/

    private String login;
    private String name;
    private String active;
    private String email;

    /*---------- CONSTRUCTEURS ----------*/

    public User(String login, String name, String active, String email)
    {
        this.login = login;
        this.name = name;
        this.active = active;
        this.email = email;
    }

    public User()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "login")
    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "active")
    public String getActive()
    {
        return active;
    }

    public void setActive(String active)
    {
        this.active = active;
    }

    @XmlAttribute(name = "email")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}

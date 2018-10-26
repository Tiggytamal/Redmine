package model.sonarapi67;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

@XmlRootElement
public class Connexion extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String login;
    private String password;

    /*---------- CONSTRUCTEURS ----------*/

    public Connexion(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
    
    public Connexion()
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

    @XmlAttribute(name = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}

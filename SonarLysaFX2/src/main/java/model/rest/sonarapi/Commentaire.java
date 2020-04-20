package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele pour les commentaires en retour du wervice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Commentaire extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String login;
    private String htmlText;
    private String markdown;
    private String updatable;
    private String createdAt;

    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }
    
    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "login")
    public String getLogin()
    {
        return getString(login);
    }
    
    public void setLogin(String login)
    {
        this.login = getString(login);
    }

    @XmlAttribute(name = "htmlText")
    public String getHtmlText()
    {
        return getString(htmlText);
    }
    
    public void setHtmlText(String htmlText)
    {
        this.htmlText = getString(htmlText);
    }

    @XmlAttribute(name = "markdown")
    public String getMarkdown()
    {
        return getString(markdown);
    }
    
    public void setMarkdown(String markdown)
    {
        this.markdown = getString(markdown);
    }

    @XmlAttribute(name = "updatable")
    public String getUpdatable()
    {
        return getString(updatable);
    }
    
    public void setUpdatable(String updatable)
    {
        this.updatable = getString(updatable);
    }

    @XmlAttribute(name = "createdAt")
    public String getCreatedAt()
    {
        return getString(createdAt);
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = getString(createdAt);
    }    
}

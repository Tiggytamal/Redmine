package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant une règle en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Rule extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String name;
    private String status;
    private String lang;
    private String langName;

    /*---------- CONSTRUCTEURS ----------*/

    public Rule(String key, String name, String status, String lang, String langName)
    {
        super();
        this.key = key;
        this.name = name;
        this.status = status;
        this.lang = lang;
        this.langName = langName;
    }

    public Rule()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "name")
    public String getName()
    {
        return getString(name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "status")
    public String getStatus()
    {
        return getString(status);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @XmlAttribute(name = "lang")
    public String getLang()
    {
        return getString(lang);
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    @XmlAttribute(name = "langName")
    public String getLangName()
    {
        return getString(langName);
    }

    public void setLangName(String langName)
    {
        this.langName = langName;
    }
}

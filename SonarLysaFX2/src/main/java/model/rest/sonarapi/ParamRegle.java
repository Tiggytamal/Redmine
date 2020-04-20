package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de odele représentant un paramètre d'une regle SonarQube depuis le webservice
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class ParamRegle extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String htmlDesc;
    private String defaultValue;
    private String type;
    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "value", required = false)
    public String getValeur()
    {
        return getString(valeur);
    }

    public void setValeur(String valeur)
    {
        this.valeur = getString(valeur);
    }

    @XmlAttribute(name = "key", required = false)
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "htmlDesc", required = false)
    public String getHtmlDesc()
    {
        return getString(htmlDesc);
    }

    public void setHtmlDesc(String htmlDesc)
    {
        this.htmlDesc = getString(htmlDesc);
    }

    @XmlAttribute(name = "defaultValue", required = false)
    public String getDefaultValue()
    {
        return getString(defaultValue);
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = getString(defaultValue);
    }

    @XmlAttribute(name = "type", required = false)
    public String getType()
    {
        return getString(type);
    }

    public void setType(String type)
    {
        this.type = getString(type);
    }
}

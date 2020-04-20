package model.rest.sonarapi67;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de od�le repr�sentant un param�tre d'une r�gle SonarQube depuis le webservice
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @Since 1.0
 *
 */
@XmlRootElement
public class Param extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String htmlDesc;
    private String defaultValue;
    private String type;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key", required = false)
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "htmlDesc", required = false)
    public String getHtmlDesc()
    {
        return htmlDesc;
    }

    public void setHtmlDesc(String htmlDesc)
    {
        this.htmlDesc = htmlDesc;
    }

    @XmlAttribute(name = "defaultValue", required = false)
    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @XmlAttribute(name = "type", required = false)
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}

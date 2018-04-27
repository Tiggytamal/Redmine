package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AjouterVueLocale implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String refKey;

    /*---------- CONSTRUCTEURS ----------*/

    public AjouterVueLocale(String key, String refKey)
    {
        this.key = key;
        this.refKey = refKey;
    }

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key", required = true)
    public String getKey()
    {
        return key;
    }

    @XmlAttribute(name = "ref_key", required = true)
    public String getRefKey()
    {
        return refKey;
    }
}
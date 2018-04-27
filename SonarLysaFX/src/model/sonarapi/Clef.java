package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Clef implements ModeleSonar
{
    private String key;

    public Clef(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

}

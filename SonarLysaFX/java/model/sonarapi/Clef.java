package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Clef implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;

    /*---------- CONSTRUCTEURS ----------*/

    public Clef(String key)
    {
        this.key = key;
    }
    
    public Clef()
    {
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML        
    }

    /*---------- ACCESSEURS ----------*/
    
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

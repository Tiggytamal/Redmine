package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

@XmlRootElement
public class Clef extends AbstractModele implements ModeleSonar
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
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML        
    }

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
    
}

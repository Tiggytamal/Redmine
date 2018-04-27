package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Modèle des QualityGate
 * 
 * @author ETP8137 - Grégoire mathon
 *
 */
@XmlRootElement
public class QualityGate
{
    /*---------- ATTRIBUTS ----------*/

    private String id;
    private String name;
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @XmlAttribute (name = "id")
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    
    @XmlAttribute (name = "name")
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }   
}
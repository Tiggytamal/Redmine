package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Mod�le des QualityGate
 * 
 * @author ETP8137 - Gr�goire mathon
 *
 */
@XmlRootElement
public class QualityGate
{
    /*---------- ATTRIBUTS ----------*/

    private String id;
    private String name;

    /*---------- CONSTRUCTEURS ----------*/

    public QualityGate(String id, String name)
    {
        super();
        this.id = id;
        this.name = name;
    }

    public QualityGate()
    {
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id")
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
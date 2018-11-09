package model.rest.sonarapi67;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant un Event en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Event extends AbstractModele implements ModeleSonar
{

    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String category;
    private String name;
    private String description;

    /*---------- CONSTRUCTEURS ----------*/

    public Event(String key, String category, String name, String description)
    {
        super();
        this.key = key;
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public Event()
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

    @XmlAttribute(name = "category")
    public String getCategory()
    {
        return getString(category);
    }

    public void setCategory(String category)
    {
        this.category = category;
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

    @XmlAttribute(name = "description")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}

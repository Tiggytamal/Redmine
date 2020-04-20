package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele représentant un Event en retour du webservice Sonar.
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
    private String categorie;
    private String nom;
    private String description;

    /*---------- CONSTRUCTEURS ----------*/
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
        this.key = getString(key);
    }

    @XmlAttribute(name = "category")
    public String getCategorie()
    {
        return getString(categorie);
    }

    public void setCategorie(String category)
    {
        this.categorie = getString(category);
    }

    @XmlAttribute(name = "name")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String name)
    {
        this.nom = getString(name);
    }

    @XmlAttribute(name = "description")
    public String getDescription()
    {
        return getString(description);
    }

    public void setDescription(String description)
    {
        this.description = getString(description);
    }
}

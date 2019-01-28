package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Modèle des QualityGate en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire mathon
 *
 */
@XmlRootElement
public class QualityProfile extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String id;
    private String name;

    /*---------- CONSTRUCTEURS ----------*/

    public QualityProfile(String id, String name)
    {
        super();
        this.id = id;
        this.name = name;
    }

    public QualityProfile()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id")
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
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
}

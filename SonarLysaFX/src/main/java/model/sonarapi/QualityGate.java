package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Modèle des QualityGate en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire mathon
 *
 */
@XmlRootElement
public class QualityGate extends AbstractModele implements ModeleSonar
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

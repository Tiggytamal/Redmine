package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe modèle pour ajouter un projet dans Sonar depuis le WebService.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class AjouterProjet extends AbstractModele implements ModeleSonar
{

    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String projectKey;

    /*---------- CONSTRUCTEURS ----------*/

    public AjouterProjet(String key, String projectKey)
    {
        this.key = key;
        this.projectKey = projectKey;
    }
    
    public AjouterProjet()
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

    @XmlAttribute(name = "project_key")
    public String getProjectKey()
    {
        return getString(projectKey);
    }
    
    public void setProjectKey(String projectKey)
    {
        this.projectKey = projectKey;
    }
}

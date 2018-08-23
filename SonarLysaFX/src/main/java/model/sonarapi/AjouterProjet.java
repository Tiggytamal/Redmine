package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

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

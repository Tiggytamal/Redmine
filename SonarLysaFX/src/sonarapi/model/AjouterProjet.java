package sonarapi.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AjouterProjet implements ModeleSonar
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

    @XmlAttribute(name = "project_key")
    public String getProjectKey()
    {
        return projectKey;
    }

    public void setProjectKey(String projectKey)
    {
        this.projectKey = projectKey;
    }
}
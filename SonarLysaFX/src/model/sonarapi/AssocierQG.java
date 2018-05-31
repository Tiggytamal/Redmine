package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AssocierQG implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String gateId;
    private String projectId;

    /*---------- CONSTRUCTEURS ----------*/

    public AssocierQG(String gateId, String projetId)
    {
        this.gateId = gateId;
        this.projectId = projetId;
    }
    
    public AssocierQG()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute (name = "gateId")
    public String getGateId()
    {
        return gateId;
    }
    
    public void setGateId(String gateId)
    {
        this.gateId = gateId;
    }

    @XmlAttribute (name = "projectId")
    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }
    
}

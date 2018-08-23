package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

@XmlRootElement
public class AssocierQG extends AbstractModele implements ModeleSonar
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
        return getString(gateId);
    }
    
    public void setGateId(String gateId)
    {
        this.gateId = gateId;
    }

    @XmlAttribute (name = "projectId")
    public String getProjectId()
    {
        return getString(projectId);
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }
    
}

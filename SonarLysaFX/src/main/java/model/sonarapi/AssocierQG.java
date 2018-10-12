package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de mod�le pour associer un Quality Gate depuis le websrvice Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
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
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
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

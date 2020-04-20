package model.rest.sonarapi67;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

@XmlRootElement
public class Projet extends AbstractModele implements ModeleSonar
{
    private String projetId;
    
    public Projet(String projetId)
    {
        this.projetId = projetId;
    }
    
    public Projet()
    {
        
    }

    @XmlAttribute(name = "project")
    public String getProjet()
    {
        return projetId;
    }

    public void setProjet(String projetId)
    {
        this.projetId = projetId;
    }

}

package model.rest.sonarapi67;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de retour du webservice pour récupérer les QUalityProfile
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class QualityProfiles extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    Action actions;
    List<QualityProfile> profiles;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "actions", required = false)
    public Action getActions()
    {
        return actions;
    }

    public void setActions(Action actions)
    {
        this.actions = actions;
    }

    @XmlAttribute(name = "profiles", required = false)
    @XmlElementWrapper
    public List<QualityProfile> getProfiles()
    {
        return profiles;
    }

    public void setProfiles(List<QualityProfile> profiles)
    {
        this.profiles = profiles;
    }
}

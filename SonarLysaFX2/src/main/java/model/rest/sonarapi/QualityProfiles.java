package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de retour du webservice pour récupérer les QUalityProfile
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
@JsonIgnoreProperties({ "actions" })
public class QualityProfiles extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private List<QualityProfile> profiles;

    /*---------- CONSTRUCTEURS ----------*/

    public QualityProfiles()
    {
        profiles = new ArrayList<>();
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "profiles", required = false)
    @XmlElementWrapper
    public List<QualityProfile> getProfiles()
    {
        if (profiles == null)
            profiles = new ArrayList<>();
        return profiles;
    }

    public void setProfiles(List<QualityProfile> profiles)
    {
        if (profiles != null && !profiles.isEmpty())
            this.profiles = profiles;
    }
}

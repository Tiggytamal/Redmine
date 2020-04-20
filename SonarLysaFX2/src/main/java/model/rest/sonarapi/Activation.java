package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de modele représentant une activation d'une regle SonarQube sur un profil donne
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@XmlRootElement
@JsonIgnoreProperties(
{ "createdAt" })
public class Activation extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String qProfile;
    private String inherit;
    private String severity;
    private List<ParamRegle> parametres;

    /*---------- CONSTRUCTEURS ----------*/

    public Activation()
    {
        parametres = new ArrayList<>();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "qProfile", required = false)
    public String getqProfile()
    {
        return getString(qProfile);
    }

    public void setqProfile(String qProfile)
    {
        this.qProfile = getString(qProfile);
    }

    @XmlAttribute(name = "inherit", required = false)
    public String getInherit()
    {
        return getString(inherit);
    }

    public void setInherit(String inherit)
    {
        this.inherit = getString(inherit);
    }

    @XmlAttribute(name = "severity", required = false)
    public String getSeverity()
    {
        return getString(severity);
    }

    public void setSeverity(String severity)
    {
        this.severity = getString(severity);
    }

    @XmlElementWrapper
    @XmlAttribute(name = "params", required = false)
    public List<ParamRegle> getParametres()
    {
        if (parametres == null)
            parametres = new ArrayList<>();
        return parametres;
    }

    public void setParametres(List<ParamRegle> parametres)
    {
        if (parametres != null && !parametres.isEmpty())
            this.parametres = parametres;
    }
}

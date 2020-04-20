package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele represenant le retour des detils d'une regle SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@XmlRootElement
public class DetailRegle extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private Regle regle;
    private List<Activation> activations;

    /*---------- CONSTRUCTEURS ----------*/

    public DetailRegle()
    {
        activations = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "rule", required = true)
    public Regle getRegle()
    {
        return regle;
    }

    public void setRegle(Regle regle)
    {
        if (regle != null)
            this.regle = regle;
    }

    @XmlAttribute(name = "actives", required = false)
    @XmlElementWrapper
    public List<Activation> getActivations()
    {
        if (activations == null)
            activations = new ArrayList<>();
        return activations;
    }

    public void setActivations(List<Activation> activations)
    {
        if (activations != null && !activations.isEmpty())
            this.activations = activations;
    }

}

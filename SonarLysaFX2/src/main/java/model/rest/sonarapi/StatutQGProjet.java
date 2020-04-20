package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de modele représentant le status d'un projet en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
@JsonIgnoreProperties({ "ignoredConditions" })
public class StatutQGProjet extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String status;
    private List<StatutQGCondition> conditions;
    private List<StatutPeriode> periodes;

    /*---------- CONSTRUCTEURS ----------*/
    
    public StatutQGProjet()
    {
        conditions = new ArrayList<>();
        periodes = new ArrayList<>();
    }

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "status")
    public String getStatus()
    {
        return getString(status);
    }

    public void setStatus(String status)
    {
        this.status = getString(status);
    }

    @XmlAttribute(name = "conditions")
    public List<StatutQGCondition> getConditions()
    {
        if (conditions == null)
            conditions = new ArrayList<>();
        return conditions;
    }

    public void setConditions(List<StatutQGCondition> conditions)
    {
        if (conditions != null && !conditions.isEmpty())
            this.conditions = conditions;
    }

    @XmlAttribute(name = "periods")
    public List<StatutPeriode> getPeriodes()
    {
        if (periodes == null)
            periodes = new ArrayList<>();
        return periodes;
    }

    public void setPeriodes(List<StatutPeriode> periodes)
    {
        if (periodes != null && !periodes.isEmpty())
            this.periodes = periodes;
    }
}

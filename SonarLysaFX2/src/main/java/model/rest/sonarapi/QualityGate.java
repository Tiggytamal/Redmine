package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de modele d'un qualityGate renvoyee par le webservice
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
@JsonIgnoreProperties(
{ "actions" })
public class QualityGate extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String id;
    private String nom;
    private boolean isDefault;
    private boolean isBuiltIn;
    private List<ConditionQG> conditions;

    /*---------- CONSTRUCTEURS ----------*/

    public QualityGate()
    {
        conditions = new ArrayList<>();
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id", required = true)
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = getString(id);
    }

    @XmlAttribute(name = "name", required = true)
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlAttribute(name = "isDefault", required = false)
    public boolean isDefault()
    {
        return isDefault;
    }

    public void setDefault(boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    @XmlAttribute(name = "isBuiltIn", required = false)
    public boolean isBuiltIn()
    {
        return isBuiltIn;
    }

    public void setBuiltIn(boolean isBuiltIn)
    {
        this.isBuiltIn = isBuiltIn;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "conditions", required = false)
    public List<ConditionQG> getConditions()
    {
        if (conditions == null)
            conditions = new ArrayList<>();
        return conditions;
    }

    public void setConditions(List<ConditionQG> conditions)
    {
        if (conditions != null && !conditions.isEmpty())
            this.conditions = conditions;
    }
}

package model.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatusProjet implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String status;
    private List<Condition> conditions;
    private List<StatusPeriode> periodes;

    /*---------- CONSTRUCTEURS ----------*/

    public StatusProjet(String status, List<Condition> conditions, List<StatusPeriode> periodes)
    {
        this.status = status;
        this.conditions = conditions;
        this.periodes = periodes;
    }

    public StatusProjet()
    {
        conditions = new ArrayList<>();
        periodes = new ArrayList<>();
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "status")
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @XmlAttribute(name = "conditions")
    public List<Condition> getConditions()
    {
        return conditions;
    }

    public void setConditions(List<Condition> conditions)
    {
        this.conditions = conditions;
    }

    @XmlAttribute(name = "periods")
    public List<StatusPeriode> getPeriodes()
    {
        return periodes;
    }

    public void setPeriodes(List<StatusPeriode> periodes)
    {
        this.periodes = periodes;
    }
}

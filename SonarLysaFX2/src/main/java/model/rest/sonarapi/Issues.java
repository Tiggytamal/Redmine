package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de modele représentant une liste d'anomalies en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
@JsonIgnoreProperties(
{ "components", "rules", "languages" })
public class Issues extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int total;
    private int p;
    private int ps;
    private Paging paging;
    private List<ComposantSonar> composants;
    private List<Issue> listIssues;
    private List<User> utilisateurs;

    /*---------- CONSTRUCTEURS ----------*/

    public Issues()
    {
        composants = new ArrayList<>();
        listIssues = new ArrayList<>();
        utilisateurs = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "total")
    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    @XmlAttribute(name = "p")
    public int getP()
    {
        return p;
    }

    public void setP(int p)
    {
        this.p = p;
    }

    @XmlAttribute(name = "ps")
    public int getPs()
    {
        return ps;
    }

    public void setPs(int ps)
    {
        this.ps = ps;
    }

    @XmlAttribute(name = "paging")
    public Paging getPaging()
    {
        if (paging == null)
            return new Paging();
        return paging;
    }

    public void setPaging(Paging paging)
    {
        if (paging != null)
            this.paging = paging;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "components", required = false)
    public List<ComposantSonar> getComposants()
    {
        if (composants == null)
            composants = new ArrayList<>();
        return composants;
    }

    public void setComposants(List<ComposantSonar> composants)
    {
        if (composants != null && !composants.isEmpty())
            this.composants = composants;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "issues", required = false)
    public List<Issue> getListIssues()
    {
        if (listIssues == null)
            listIssues = new ArrayList<>();
        return listIssues;
    }

    public void setListIssues(List<Issue> listIssues)
    {
        if (listIssues != null && !listIssues.isEmpty())
            this.listIssues = listIssues;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "users", required = false)
    public List<User> getUtilisateurs()
    {
        if (utilisateurs == null)
            utilisateurs = new ArrayList<>();
        return utilisateurs;
    }

    public void setUtilisateurs(List<User> utilisateurs)
    {
        if (utilisateurs != null && !utilisateurs.isEmpty())
            this.utilisateurs = utilisateurs;
    }
}

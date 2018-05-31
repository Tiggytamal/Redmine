package model.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties({"components"})
public class Issues implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int total;
    private int p;
    private int ps;
    private Paging paging;
    private List<Composant> composants;
    private List<Issue> listIssues;

    /*---------- CONSTRUCTEURS ----------*/
    
    public Issues(int total, int p, int ps, Paging paging, List<Composant> composants, List<Issue> listIssues)
    {
        this.total = total;
        this.p = p;
        this.ps = ps;
        this.paging = paging;
        this.composants = composants;
        this.listIssues = listIssues;
    }
    
    public Issues()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML        
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
        return paging;
    }

    public void setPaging(Paging paging)
    {
        this.paging = paging;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "components", required = false)
    public List<Composant> getComposants()
    {
        if (composants == null)
            return new ArrayList<>();
        return composants;
    }

    public void setComposants(List<Composant> composants)
    {
        this.composants = composants;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "issues", required = false)
    public List<Issue> getListIssues()
    {
        if (listIssues == null)
            return new ArrayList<>();
        return listIssues;
    }

    public void setListIssues(List<Issue> listIssues)
    {
        this.listIssues = listIssues;
    }
}

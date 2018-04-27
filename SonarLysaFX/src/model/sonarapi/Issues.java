package model.sonarapi;

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
        return composants;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "issues", required = false)
    public List<Issue> getListIssues()
    {
        return listIssues;
    }

    public void setListIssues(List<Issue> listIssues)
    {
        this.listIssues = listIssues;
    }
}

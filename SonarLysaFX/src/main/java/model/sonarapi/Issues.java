package model.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Classe de modèle représentant une liste d'anomalies en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
@JsonIgnoreProperties({ "components" })
public class Issues extends AbstractModele implements ModeleSonar
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

    @XmlAttribute(name = "p")
    public int getP()
    {
        return p;
    }

    @XmlAttribute(name = "ps")
    public int getPs()
    {
        return ps;
    }

    @XmlAttribute(name = "paging")
    public Paging getPaging()
    {
        if (paging == null)
            return new Paging();
        return paging;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "components", required = false)
    public List<Composant> getComposants()
    {
        return composants == null ? composants = new ArrayList<>() : composants;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "issues", required = false)
    public List<Issue> getListIssues()
    {
        return listIssues == null ? listIssues = new ArrayList<>() : listIssues;
    }
}

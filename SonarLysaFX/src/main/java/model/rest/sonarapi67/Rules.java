package model.rest.sonarapi67;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;
import model.rest.sonarapi.Paging;

/**
 * Classe de wrapping des règles lors du retour du webservice SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Rules extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int total;
    private int p;
    private int ps;
    private Paging paging;
    private List<Rule> listRules;

    /*---------- CONSTRUCTEURS ----------*/
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

    @XmlElement(name = "paging")
    public Paging getPaging()
    {
        if (paging == null)
            return new Paging();
        return paging;
    }

    @XmlAttribute(name = "rules")
    @XmlElementWrapper
    public List<Rule> getListRules()
    {
        if (listRules == null)
            listRules = new ArrayList<>();
        return listRules;
    }

    public void setListRules(List<Rule> rules)
    {
        if (rules != null)
            this.listRules = rules;
    }

}

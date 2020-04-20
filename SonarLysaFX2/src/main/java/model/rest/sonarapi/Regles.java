package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de wrapping des régles lors du retour du webservice SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Regles extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int total;
    private int p;
    private int ps;
    private Paging paging;
    private List<Regle> listRegles;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    public Regles()
    {
        listRegles = new ArrayList<>();
    }
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

    @XmlElement(name = "paging")
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

    @XmlAttribute(name = "rules")
    @XmlElementWrapper
    public List<Regle> getListRegles()
    {
        if (listRegles == null)
            listRegles = new ArrayList<>();
        return listRegles;
    }

    public void setListRegles(List<Regle> regles)
    {
        if (regles != null && !regles.isEmpty())
            this.listRegles = regles;
    }

}

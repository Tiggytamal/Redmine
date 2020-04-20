package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele représentant une liste d'analyses en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Analyses extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private Paging paging;
    private List<Analyse> listAnalyses;

    /*---------- CONSTRUCTEURS ----------*/

    public Analyses()
    {
        listAnalyses = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "paging")
    public Paging getPaging()
    {
        return paging;
    }

    public void setPaging(Paging paging)
    {
        if (paging != null)
            this.paging = paging;
    }

    @XmlAttribute(name = "analyses", required = false)
    @XmlElementWrapper
    public List<Analyse> getListAnalyses()
    {
        if (listAnalyses == null)
            listAnalyses = new ArrayList<>();
        return listAnalyses;
    }

    public void setListAnalyses(List<Analyse> listAnalyses)
    {
        if (listAnalyses != null && !listAnalyses.isEmpty())
            this.listAnalyses = listAnalyses;
    }
}

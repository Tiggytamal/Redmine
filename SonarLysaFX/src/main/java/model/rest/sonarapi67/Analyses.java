package model.rest.sonarapi67;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;
import model.rest.sonarapi.Paging;

/**
 * Classe de modèle représentant une liste d'analyses en retour du webservice Sonar.
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

    public Analyses(Paging paging, List<Analyse> listAnalyses)
    {
        this.paging = paging;
        this.listAnalyses = listAnalyses;
    }

    public Analyses()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
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

    public void setListAnalyses(List<Analyse> analyses)
    {
        this.listAnalyses = analyses;
    }
}

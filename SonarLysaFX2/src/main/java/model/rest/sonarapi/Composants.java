package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de retour du webervice Sonar rpresentant une liste de composants.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
@JsonIgnoreProperties(
{ "facets", "organizations" })
public class Composants extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private Paging paging;
    private List<ComposantSonar> listeComposants;

    /*---------- CONSTRUCTEURS ----------*/

    public Composants()
    {
        listeComposants = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlElement(name = "paging", required = true)
    public Paging getPaging()
    {
        if (paging == null)
            paging = new Paging();
        return paging;
    }

    public void setPaging(Paging paging)
    {
        if (paging != null)
            this.paging = paging;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "components", required = false)
    public List<ComposantSonar> getListeComposants()
    {
        if (listeComposants == null)
            listeComposants = new ArrayList<>();
        return listeComposants;
    }

    public void setListeComposants(List<ComposantSonar> listeComposants)
    {
        if (listeComposants != null && !listeComposants.isEmpty())
            this.listeComposants = listeComposants;
    }
}

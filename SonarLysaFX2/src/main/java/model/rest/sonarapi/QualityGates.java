package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de modele du retour de la liste des QualityGates depuis le webservice
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
@JsonIgnoreProperties({ "actions" })
public class QualityGates extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private List<QualityGate> listeQualityGates;
    private int defaut;

    /*---------- CONSTRUCTEURS ----------*/

    public QualityGates()
    {
        listeQualityGates = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlAttribute(name = "qualitygates", required = true)
    public List<QualityGate> getListeQualityGates()
    {
        if (listeQualityGates == null)
            listeQualityGates = new ArrayList<>();
        return listeQualityGates;
    }

    public void setListeQualityGates(List<QualityGate> listeQualityGates)
    {
        if (listeQualityGates != null && !listeQualityGates.isEmpty())
            this.listeQualityGates = listeQualityGates;
    }

    @XmlAttribute(name = "default", required = true)
    public int getDefaut()
    {
        return defaut;
    }

    public void setDefaut(int defaut)
    {
        this.defaut = defaut;
    }
}

package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.AbstractModele;
import model.enums.QG;
import utilities.adapter.QGAdapter;

/**
 * Classe de modele représentant le statut d'une branche renvoye par le webservice
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement(name = "status")
public class StatutBranche extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private QG qualityGate;
    private int bugs;
    private int vulnerabilites;
    private int codeSmells;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "qualityGateStatus", required = false)
    @XmlJavaTypeAdapter(value = QGAdapter.class)
    public QG getQualityGate()
    {
        return qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        if (qualityGate != null)
            this.qualityGate = qualityGate;
    }

    @XmlAttribute(name = "bugs", required = false)
    public int getBugs()
    {
        return bugs;
    }

    public void setBugs(int bugs)
    {
        this.bugs = bugs;
    }

    @XmlAttribute(name = "vulnerabilities", required = false)
    public int getVulnerabilites()
    {
        return vulnerabilites;
    }

    public void setVulnerabilites(int vulnerabilites)
    {
        this.vulnerabilites = vulnerabilites;
    }

    @XmlAttribute(name = "codeSmells", required = false)
    public int getCodeSmells()
    {
        return codeSmells;
    }

    public void setCodeSmells(int codeSmells)
    {
        this.codeSmells = codeSmells;
    }
}

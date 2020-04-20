package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de retour du webservice du detail des conditions d'un test d'un QualityGate
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@XmlRootElement (name = "Condition")
public class ConditionQG extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int id;
    private String metrique;
    private String operateur;
    private int periode;
    private String seuilWarning;
    private String seuilErreur;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute (name = "id")
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @XmlAttribute (name = "metric")
    public String getMetrique()
    {
        return getString(metrique);
    }

    public void setMetrique(String metrique)
    {
        this.metrique = getString(metrique);
    }

    @XmlAttribute (name = "op")
    public String getOperateur()
    {
        return getString(operateur);
    }

    public void setOperateur(String operateur)
    {
        this.operateur = getString(operateur);
    }
    
    @XmlAttribute (name = "period")
    public int getPeriode()
    {
        return periode;
    }

    public void setPeriode(int periode)
    {
        this.periode = periode;
    }
    
    @XmlAttribute (name = "warning")
    public String getSeuilWarning()
    {
        return getString(seuilWarning);
    }

    public void setSeuilWarning(String seuilWarning)
    {
        this.seuilWarning = getString(seuilWarning);
    }

    @XmlAttribute (name = "error")
    public String getSeuilErreur()
    {
        return getString(seuilErreur);
    }

    public void setSeuilErreur(String seuilErreur)
    {
        this.seuilErreur = getString(seuilErreur);
    }
}

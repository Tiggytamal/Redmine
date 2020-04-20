package model.rest.sonarapi;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.AbstractModele;
import utilities.adapter.LocalDateTimeSonarAdapter;

/**
 * Classe de modele représentant le status d'une anomalie à la periode donnée en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement(name = "period")
public class StatutPeriode extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int index;
    private String mode;
    private LocalDateTime date;
    private String parameter;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute
    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    @XmlAttribute
    public String getMode()
    {
        return getString(mode);
    }

    public void setMode(String mode)
    {
        this.mode = getString(mode);
    }

    @XmlAttribute
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDate()
    {
        return date;
    }

    public void setDate(LocalDateTime date)
    {
        if (date != null)
            this.date = date;
    }

    @XmlAttribute
    public String getParameter()
    {
        return getString(parameter);
    }

    public void setParameter(String parameter)
    {
        this.parameter = getString(parameter);
    }
}

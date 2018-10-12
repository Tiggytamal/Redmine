package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant le status d'une anomalie à la période donnée en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class StatusPeriode extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int index;
    private String mode;
    private String date;
    private String parameter;

    /*---------- CONSTRUCTEURS ----------*/

    public StatusPeriode(int index, String mode, String date, String parameter)
    {
        this.index = index;
        this.mode = mode;
        this.date = date;
        this.parameter = parameter;
    }

    public StatusPeriode()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

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
        this.mode = mode;
    }

    @XmlAttribute
    public String getDate()
    {
        return getString(date);
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @XmlAttribute
    public String getParameter()
    {
        return getString(parameter);
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }
}

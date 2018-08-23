package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

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
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
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

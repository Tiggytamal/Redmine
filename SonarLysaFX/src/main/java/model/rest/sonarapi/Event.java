package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant un Event en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Event extends AbstractModele implements ModeleSonar
{

    /*---------- ATTRIBUTS ----------*/

    private String id;
    private String rk;
    private String n;
    private String c;
    private String dt;

    /*---------- CONSTRUCTEURS ----------*/

    public Event(String id, String rk, String n, String c, String dt)
    {
        super();
        this.id = id;
        this.rk = rk;
        this.n = n;
        this.c = c;
        this.dt = dt;
    }

    public Event()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id")
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "rk")
    public String getRk()
    {
        return getString(rk);
    }

    public void setRk(String rk)
    {
        this.rk = rk;
    }

    @XmlAttribute(name = "n")
    public String getN()
    {
        return getString(n);
    }

    public void setN(String n)
    {
        this.n = n;
    }

    @XmlAttribute(name = "c")
    public String getC()
    {
        return getString(c);
    }

    public void setC(String c)
    {
        this.c = c;
    }

    @XmlAttribute(name = "dt")
    public String getDt()
    {
        return getString(dt);
    }

    public void setDt(String dt)
    {
        this.dt = dt;
    }
}

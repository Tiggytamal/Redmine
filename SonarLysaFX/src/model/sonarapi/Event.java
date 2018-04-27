package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event implements ModeleSonar
{

    /*---------- ATTRIBUTS ----------*/

    private String id;
    private String rk;
    private String n;
    private String c;
    private String dt;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id")
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "rk")
    public String getRk()
    {
        return rk;
    }

    public void setRk(String rk)
    {
        this.rk = rk;
    }

    @XmlAttribute(name = "n")
    public String getN()
    {
        return n;
    }

    public void setN(String n)
    {
        this.n = n;
    }

    @XmlAttribute(name = "c")
    public String getC()
    {
        return c;
    }

    public void setC(String c)
    {
        this.c = c;
    }

    public String getDt()
    {
        return dt;
    }

    @XmlAttribute(name = "dt")
    public void setDt(String dt)
    {
        this.dt = dt;
    }
}

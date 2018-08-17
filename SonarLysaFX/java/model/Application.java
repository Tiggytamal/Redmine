package model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Classe de modèle pour les applications CATS
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
@XmlRootElement
public class Application implements Modele, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String code;
    private boolean actif;
    private String libelle;
    private boolean open;
    private boolean mainFrame;
    private String valSecurite;
    private int nbreVulnerabilites;
    private int ldcSonar;
    private int ldcMainframe;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * 
     * @param valeur
     */
    public void ajouterldcSonar(int valeur)
    {
        ldcSonar += valeur;
    }
    
    /**
     * 
     * @param valeur
     */
    public void ajouterVulnerabilites(int valeur)
    {
        nbreVulnerabilites += valeur;
    }

    /**
     * 
     * @param securite
     */
    public void majValSecurite(int securite)
    {
        String secuString = convert(securite);
        if (getValSecurite().compareTo(secuString) < 0)
            valSecurite = secuString;
    }

    /*---------- METHODES PRIVEES ----------*/

    private String convert(int securite)
    {
        switch (securite)
        {
            case 0:
                return EMPTY;
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            default:
                return "F";
        }
    }

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute (name = "code", required = true)
    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @XmlAttribute (name = "actif", required = true)
    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

    @XmlAttribute (name = "libelle", required = true)
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    @XmlAttribute (name = "open", required = false)
    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    @XmlAttribute (name = "mainFrame", required = false)
    public boolean isMainFrame()
    {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame)
    {
        this.mainFrame = mainFrame;
    }

    @XmlTransient
    public String getValSecurite()
    {
        return getString(valSecurite);
    }

    public void setValSecurite(String valSecurite)
    {
        this.valSecurite = valSecurite;
    }

    @XmlTransient
    public int getNbreVulnerabilites()
    {
        return nbreVulnerabilites;
    }

    public void setNbreVulnerabilites(int nbreVulnerabilites)
    {
        this.nbreVulnerabilites = nbreVulnerabilites;
    }

    @XmlTransient
    public int getLDCSonar()
    {
        return ldcSonar;
    }

    public void setLDCSonar(int ldcSonar)
    {
        this.ldcSonar = ldcSonar;
    }

    @XmlTransient
    public int getLDCMainframe()
    {
        return ldcMainframe;
    }

    public void setLDCMainframe(int ldcMainframe)
    {
        this.ldcMainframe = ldcMainframe;
    }
}

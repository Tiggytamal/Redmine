package model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import model.enums.EtatAppli;
import model.utilities.AbstractModele;

/**
 * Classe de modèle réprésentant un composant SonarQube du fichier XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class ComposantSonar extends AbstractModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String nom;
    private String lot;
    private String key;
    private String id;
    private String appli;
    private String edition;
    private EtatAppli etatAppli;
    private int ldc;
    private int security;
    private int vulnerabilites;
    
    /*---------- CONSTRUCTEURS ----------*/

    ComposantSonar() 
    {
        if (etatAppli == null)
        etatAppli = EtatAppli.OK;
    }
    
    ComposantSonar(String id, String key, String nom)
    {
        this.id = id;
        this.key = key;
        this.nom = nom;
    }
    
    /**
     * Constructeur pour créer un clone d'un composant Sonar
     * 
     * @param clone
     */
    ComposantSonar(ComposantSonar clone)
    {
        this.nom = clone.nom;
        this.lot = clone.lot;
        this.key = clone.key;
        this.id = clone.id;
        this.appli = clone.appli;
        this.edition = clone.edition;
        this.etatAppli = clone.etatAppli;
        this.ldc = clone.ldc;
        this.security = clone.security;
        this.vulnerabilites = clone.vulnerabilites;       
    }
  
    /*---------- METHODES PUBLIQUES ----------*/  
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @XmlAttribute (name = "nom", required = true)
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute (name = "lot", required = false)
    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    @XmlAttribute (name = "key", required = true)
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute (name = "appli", required = false)
    public String getAppli()
    {
        return getString(appli);
    }

    public void setAppli(String appli)
    {
        this.appli = appli;
    }
    
    @XmlAttribute (name = "edition", required = false)
    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }
    
    @XmlAttribute (name = "id", required = true)
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
    }
    
    @XmlAttribute (name = "ldc", required = true)
    public int getLdc()
    {
        return ldc;
    }

    public void setLdc(int ldc)
    {
        this.ldc = ldc;
    }
    
    @XmlAttribute (name = "security", required = true)
    public int getSecurity()
    {
        return security;
    }

    public void setSecurity(int security)
    {
        this.security = security;
    }
    
    @XmlAttribute (name = "vulnerabilities", required = true)   
    public int getVulnerabilites()
    {
        return vulnerabilites;
    }

    public void setVulnerabilites(int vulnerabilites)
    {
        this.vulnerabilites = vulnerabilites;
    }
    
//    @XmlAttribute (name = "etatAppli", required = true) 
    @XmlTransient
    public EtatAppli getetatAppli()
    {
        return etatAppli;
    }
    
    public void setEtatAppli(EtatAppli etatAppli)
    {
        this.etatAppli = etatAppli;
    }
}

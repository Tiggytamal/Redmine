package model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.enums.EtatAppli;
import model.enums.QG;
import model.utilities.AbstractModele;
import utilities.adapter.QGAdapter;

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
    private int securityRating;
    private int vulnerabilites;
    private boolean securite;
    private QG qualityGate;
    private float bloquants;
    private float critiques;
    private float duplication;
    private boolean versionRelease;

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
        this.securityRating = clone.securityRating;
        this.vulnerabilites = clone.vulnerabilites;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "nom", required = true)
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute(name = "lot", required = false)
    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    @XmlAttribute(name = "key", required = true)
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "appli", required = false)
    public String getAppli()
    {
        return getString(appli);
    }

    public void setAppli(String appli)
    {
        this.appli = appli;
    }

    @XmlAttribute(name = "edition", required = false)
    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    @XmlAttribute(name = "id", required = true)
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "ldc", required = true)
    public int getLdc()
    {
        return ldc;
    }

    public void setLdc(int ldc)
    {
        this.ldc = ldc;
    }

    /**
     * Setter de ldc qui parse les String en int directement
     * 
     * @param ldc
     */
    public void setLdc(String ldc)
    {
        this.ldc = Integer.parseInt(ldc);
    }

    @XmlAttribute(name = "security", required = true)
    public int getSecurityRating()
    {
        return securityRating;
    }

    public void setSecurityRating(int securityRating)
    {
        this.securityRating = securityRating;
    }

    @XmlAttribute(name = "vulnerabilities", required = true)
    public int getVulnerabilites()
    {
        return vulnerabilites;
    }

    public void setVulnerabilites(int vulnerabilites)
    {
        this.vulnerabilites = vulnerabilites;
    }

    /**
     * Setter de vulnerabilite qui parse les String directement en int
     * 
     * @param vulnerabilites
     */
    public void setVulnerabilites(String vulnerabilites)
    {
        this.vulnerabilites = Integer.parseInt(vulnerabilites);
    }

    // @XmlAttribute (name = "etatAppli", required = true)
    @XmlTransient
    public EtatAppli getEtatAppli()
    {
        return etatAppli;
    }

    public void setEtatAppli(EtatAppli etatAppli)
    {
        this.etatAppli = etatAppli;
    }

    @XmlAttribute(name = "securite", required = true)
    public boolean isSecurite()
    {
        return securite;
    }

    public void setSecurite(boolean securite)
    {
        this.securite = securite;
    }

    @XmlAttribute(name = "qualityGate", required = false)
    @XmlJavaTypeAdapter(value = QGAdapter.class)
    public QG getQualityGate()
    {
        return qualityGate == null ? QG.NONE : qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        this.qualityGate = qualityGate;
    }

    /**
     * Seter du QualityGate depuis une chaîne de caratère.
     * 
     * @param qualityGate
     */
    public void setQualityGate(String qualityGate)
    {
        this.qualityGate = QG.from(qualityGate);
    }

    @XmlAttribute(name = "bloquants", required = false)
    public float getBloquants()
    {
        return bloquants;
    }

    public void setBloquants(float bloquants)
    {
        this.bloquants = bloquants;
    }

    @XmlAttribute(name = "critiques", required = false)
    public float getCritiques()
    {
        return critiques;
    }

    public void setCritiques(float critiques)
    {
        this.critiques = critiques;
    }

    @XmlAttribute(name = "duplication", required = false)
    public float getDuplication()
    {
        return duplication;
    }

    public void setDuplication(float duplication)
    {
        this.duplication = duplication;
    }

    @XmlAttribute(name = "versionRelease", required = false)
    public boolean isVersionRelease()
    {
        return versionRelease;
    }

    public void setVersionRelease(boolean versionRelease)
    {
        this.versionRelease = versionRelease;
    }
}

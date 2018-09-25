package model.bdd;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatAppli;
import model.enums.QG;

/**
 * Classe de modèle réprésentant un composant SonarQube du fichier XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity
@Table(name = "composants")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ComposantSonar.findAll", query="SELECT distinct(c) FROM ComposantSonar c "
                + "JOIN FETCH c.appli a "
                + "JOIN FETCH c.lotRTC l"),
        @NamedQuery(name="ComposantSonar.resetTable", query="DELETE FROM ComposantSonar")
})
//@formatter:on
public class ComposantSonar extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @BatchFetch(value = BatchFetchType.JOIN) 
    @ManyToOne (targetEntity = LotRTC.class, cascade = CascadeType.MERGE)
    @JoinColumn (name = "lotRTC")
    private LotRTC lotRTC;

    @Column(name = "composantKey", nullable = false, length = 256)
    private String key;

    @Column(name = "id", nullable = false)
    private String id;

    @BatchFetch(value = BatchFetchType.JOIN)    
    @ManyToOne (targetEntity = Application.class, cascade = CascadeType.MERGE)
    @JoinColumn (name = "appli")
    private Application appli;

    @Column(name = "edition", nullable = true, length = 20)
    private String edition;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatAppli", nullable = true)
    private EtatAppli etatAppli;

    @Column(name = "ldc", nullable = true)
    private int ldc;

    @Column(name = "securityRating", nullable = true, length = 1)
    private String securityRating;

    @Column(name = "vulnerabilites", nullable = true)
    private int vulnerabilites;

    @Column(name = "securite", nullable = true)
    private boolean securite;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualityGate", nullable = true)
    private QG qualityGate;

    @Column(name = "bloquants", nullable = true)
    private float bloquants;

    @Column(name = "critiques", nullable = true)
    private float critiques;

    @Column(name = "duplication", nullable = true)
    private float duplication;

    @Column(name = "versionRelease", nullable = true)
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
        this.lotRTC = clone.lotRTC;
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
    
    @Override
    public String getMapIndex()
    {
        return getKey();
    }
    
    /*---------- METHODES PRIVEES ----------*/    
    /*---------- ACCESSEURS ----------*/

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        this.lotRTC = lotRTC;
    }

    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public Application getAppli()
    {
        return appli;
    }

    public void setAppli(Application appli)
    {
        this.appli = appli;
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
    }

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

    public String getSecurityRating()
    {
        return securityRating;
    }

    public void setSecurityRating(String securityRating)
    {
        this.securityRating = securityRating;
    }
    
    public void setSecurityRatingDepuisSonar(String securityRating)
    {
        switch (securityRating)
        {
            case "0":
                setSecurityRating("A");
                break;
                
            case "1":
                setSecurityRating("A");
                break;
                
            case "2":
                setSecurityRating("B");
                break;
                
            case "3":
                setSecurityRating("C");
                break;
                
            case "4":
                setSecurityRating("D");
                break;
                
            case "5":
                setSecurityRating("E");
                break;
                
            case "6":
                setSecurityRating("F");
                break;
                
            default:
                setSecurityRating("F");
        }
    }

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

    public EtatAppli getEtatAppli()
    {
        return etatAppli;
    }

    public void setEtatAppli(EtatAppli etatAppli)
    {
        this.etatAppli = etatAppli;
    }

    public boolean isSecurite()
    {
        return securite;
    }

    public void setSecurite(boolean securite)
    {
        this.securite = securite;
    }

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

    public float getBloquants()
    {
        return bloquants;
    }

    public void setBloquants(float bloquants)
    {
        this.bloquants = bloquants;
    }

    public float getCritiques()
    {
        return critiques;
    }

    public void setCritiques(float critiques)
    {
        this.critiques = critiques;
    }

    public float getDuplication()
    {
        return duplication;
    }

    public void setDuplication(float duplication)
    {
        this.duplication = duplication;
    }

    public boolean isVersionRelease()
    {
        return versionRelease;
    }

    public void setVersionRelease(boolean versionRelease)
    {
        this.versionRelease = versionRelease;
    }
}

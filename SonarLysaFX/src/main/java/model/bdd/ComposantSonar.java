package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatAppli;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.QG;
import utilities.adapter.LocalDateAdapter;

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
                + "JOIN FETCH c.lotRTC l "
                + "LEFT JOIN FETCH c.defautAppli da"),
        @NamedQuery(name="ComposantSonar.recupLotsRTC", query="SELECT distinct(l.lot) FROM ComposantSonar c JOIN FETCH LotRTC l"),
        @NamedQuery(name="ComposantSonar.findByIndex", query="SELECT c FROM ComposantSonar c WHERE c.key = :index"),
        @NamedQuery(name="ComposantSonar.resetTable", query="DELETE FROM ComposantSonar")
})
//@formatter:on
@XmlRootElement
public class ComposantSonar extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "security_rating", nullable = true, length = 1)
    private String securityRating;
    
    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = LotRTC.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "lotRTC")
    private LotRTC lotRTC;

    @Column(name = "composant_key", nullable = false, length = 255)
    private String key;

    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "version", nullable = false, length = 32)
    private String version;

    @ManyToOne(targetEntity = Application.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "appli")
    private Application appli;

    @Column(name = "ldc", nullable = true)
    private int ldc;

    @Column(name = "vulnerabilites", nullable = true)
    private int vulnerabilites;

    @Column(name = "vrais_defauts", nullable = false)
    private int vraisDefauts;

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

    @Column(name = "version_release", nullable = true)
    private boolean versionRelease;

    @Enumerated(EnumType.STRING)
    @Column(name = "matiere", nullable = false)
    private Matiere matiere;

    @Enumerated(EnumType.STRING)
    @Column(name = "instance", nullable = false)
    private InstanceSonar instance;

    @Column(name = "date_repack", nullable = true)
    private LocalDate dateRepack;

    @OneToOne(targetEntity = DefautAppli.class, cascade = CascadeType.MERGE, mappedBy = "compo")
    private DefautAppli defautAppli;

    @Transient
    private EtatAppli etatAppli;

    /*---------- CONSTRUCTEURS ----------*/

    ComposantSonar()
    {
        etatAppli = EtatAppli.OK;
        vraisDefauts = 0;
    }

    ComposantSonar(String id, String key, String nom)
    {
        this.id = id;
        this.key = key;
        this.nom = nom;
        etatAppli = EtatAppli.OK;
        vraisDefauts = 0;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static ComposantSonar build(String id, String key, String nom)
    {
        return new ComposantSonar(id, key, nom);
    }

    @Override
    public String getMapIndex()
    {
        return getKey();
    }

    /**
     * Calcul de la valeur de sécurité d'un composant, en transformatn la valeur float envoyée par SonarQube.
     * 
     * @param securityRating
     */
    public void setSecurityRatingDepuisSonar(String securityRating)
    {
        String valeur = getString(securityRating);
        if (valeur.length() > 1)
            valeur = valeur.substring(0, 1);
        switch (valeur)
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

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "nom")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlElement(name = "lotRTC")
    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        if (lotRTC != null)
            this.lotRTC = lotRTC;
    }

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlElement(name = "appli")
    public Application getAppli()
    {
        return appli;
    }

    public void setAppli(Application appli)
    {
        if (appli != null)
            this.appli = appli;
    }

    @XmlAttribute(name = "id")
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = getString(id);
    }

    @XmlAttribute(name = "ldc")
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

    @XmlAttribute(name = "securityRating")
    public String getSecurityRating()
    {
        return getString(securityRating);
    }

    public void setSecurityRating(String securityRating)
    {
        this.securityRating = getString(securityRating);
    }

    @XmlAttribute(name = "vulnerabilites")
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
        if (vulnerabilites != null && !vulnerabilites.isEmpty())
            this.vulnerabilites = Integer.parseInt(vulnerabilites);
    }

    @XmlAttribute(name = "securite")
    public boolean isSecurite()
    {
        return securite;
    }

    public void setSecurite(boolean securite)
    {
        this.securite = securite;
    }

    @XmlAttribute(name = "qualityGate")
    public QG getQualityGate()
    {
        return qualityGate == null ? QG.NONE : qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        if (qualityGate != null)
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

    @XmlAttribute(name = "bloquants")
    public float getBloquants()
    {
        return bloquants;
    }

    public void setBloquants(float bloquants)
    {
        this.bloquants = bloquants;
    }

    @XmlAttribute(name = "critiques")
    public float getCritiques()
    {
        return critiques;
    }

    public void setCritiques(float critiques)
    {
        this.critiques = critiques;
    }

    @XmlAttribute(name = "duplication")
    public float getDuplication()
    {
        return duplication;
    }

    public void setDuplication(float duplication)
    {
        this.duplication = duplication;
    }

    @XmlAttribute(name = "versionrelease")
    public boolean isVersionRelease()
    {
        return versionRelease;
    }

    public void setVersionRelease(boolean versionRelease)
    {
        this.versionRelease = versionRelease;
    }

    @XmlAttribute(name = "matiere")
    public Matiere getMatiere()
    {
        return matiere;
    }

    public void setMatiere(Matiere matiere)
    {
        if (matiere != null)
            this.matiere = matiere;
    }

    @XmlAttribute(name = "etatAppli")
    public EtatAppli getEtatAppli()
    {
        return etatAppli;
    }

    public void setEtatAppli(EtatAppli etatAppli)
    {
        if (etatAppli != null)
            this.etatAppli = etatAppli;
    }

    @XmlAttribute(name = "instance")
    public InstanceSonar getInstance()
    {
        return instance;
    }

    public void setInstance(InstanceSonar instance)
    {
        if (instance != null)
            this.instance = instance;
    }

    @XmlAttribute(name = "version")
    public String getVersion()
    {
        return getString(version);
    }

    public void setVersion(String version)
    {

        this.version = getString(version);
    }

    @XmlAttribute(name = "dateRepack")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateRepack()
    {
        return dateRepack;
    }

    public void setDateRepack(LocalDate dateRepack)
    {
        if (dateRepack != null)
            this.dateRepack = dateRepack;
    }

    @XmlElement(name = "defautAppli")
    public DefautAppli getDefautAppli()
    {
        return defautAppli;
    }

    public void setDefautAppli(DefautAppli defautAppli)
    {
        this.defautAppli = defautAppli;
    }

    @XmlAttribute(name = "vraisDefauts")
    public int getVraisDefauts()
    {
        return vraisDefauts;
    }

    public void setVraisDefauts(int vraisDefauts)
    {
        this.vraisDefauts = vraisDefauts;
    }
}

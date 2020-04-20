package model.bdd;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dao.AbstractMySQLDao;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.QG;
import model.rest.repack.RepackREST;
import utilities.Statics;
import utilities.Utilities;
import utilities.adapter.LocalDateAdapter;
import utilities.adapter.LocalDateTimeSonarAdapter;

/**
 * Classe de modele représentant un composant SonarQube en base de données.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity(name = "ComposantBase")
@Access(AccessType.FIELD)
@Table(name = "composants")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ComposantBase" + AbstractMySQLDao.FINDALL, query="SELECT distinct(c) FROM ComposantBase c "
                + "LEFT JOIN FETCH c.appli a "
                + "LEFT JOIN FETCH c.lotRTC l "
                + "LEFT JOIN FETCH l.projetClarity pc "
                + "LEFT JOIN FETCH l.edition e "
                + "LEFT JOIN FETCH pc.chefService cs "
                + "LEFT JOIN FETCH c.solution s "),
        @NamedQuery(name="ComposantBase.keyComposTEP", query="SELECT new model.KeyDateMEP(c.key, c.nom, l.dateMajEtat) FROM ComposantBase c "
                + "JOIN FETCH LotRTC l ON c.lotRTC = l.IDBASE "
                + "WHERE l.dateMajEtat BETWEEN :dateDebut AND :dateFin "
                + "AND (l.etatLot = model.enums.EtatLot.TERMINE OR l.etatLot = model.enums.EtatLot.EDITION)"),
        @NamedQuery(name="ComposantBase.lignesTotal", query="SELECT SUM(c.ldc) FROM ComposantBase c WHERE c.instance = model.enums.InstanceSonar.LEGACY"),
        @NamedQuery(name="ComposantBase.composPropres", query="SELECT COUNT(c) FROM ComposantBase c WHERE c.propre = TRUE"),
        @NamedQuery(name="ComposantBase.recupLotsRTC", query="SELECT distinct(l.numero) FROM ComposantBase c JOIN FETCH LotRTC l"),
        @NamedQuery(name="ComposantBase" + AbstractMySQLDao.FINDINDEX, query="SELECT c FROM ComposantBase c WHERE CONCAT(c.key, c.branche) = :index"),
        @NamedQuery(name="ComposantBase.recupCompoByNom", query="SELECT c FROM ComposantBase c WHERE c.nom LIKE CONCAT('%', :index)"),        
        @NamedQuery(name="ComposantBase" + AbstractMySQLDao.RESET, query="DELETE FROM ComposantBase")
})
//@formatter:on
@XmlRootElement(name = "composant")
public class ComposantBase extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final Pattern PATTERNNUMBERNOMPO = Pattern.compile("^.*\\d\\d$");

    @Column(name = "composant_key", nullable = false)
    private String key;

    @Column(name = "branche", nullable = false, length = 32)
    private String branche;

    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualityGate", nullable = true)
    private QG qualityGate;

    @ManyToOne(targetEntity = LotRTC.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "lotRTC")
    private LotRTC lotRTC;

    @ManyToOne(targetEntity = Application.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "appli")
    private Application appli;

    @ManyToOne(targetEntity = Solution.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "solution")
    private Solution solution;

    @Column(name = "version", nullable = false, length = 32)
    private String version;

    @Column(name = "versionMax", nullable = false, length = 32)
    private String versionMax;

    @Column(name = "ldc", nullable = true)
    private int ldc;

    @Column(name = "securityRating", nullable = true, length = 1)
    private String securityRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "matiere", nullable = false)
    private Matiere matiere;

    @Column(name = "derniere_analyse", nullable = true)
    private LocalDateTime derniereAnalyse;

    @Column(name = "date_Leak_period", nullable = true)
    private LocalDateTime dateLeakPeriod;

    @Column(name = "date_repack", nullable = true)
    private LocalDate dateRepack;

    @Enumerated(EnumType.STRING)
    @Column(name = "instance", nullable = false)
    private InstanceSonar instance;

    @Column(name = "doublon", nullable = true)
    private boolean doublon;

    @Column(name = "controle_appli", nullable = true)
    private boolean controleAppli;

    @Column(name = "propre", nullable = false)
    private boolean propre;

    /*---------- CONSTRUCTEURS ----------*/

    ComposantBase()
    {
        controleAppli = true;
    }

    private ComposantBase(String key, String nom)
    {
        this();
        this.key = key;
        this.nom = nom;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static ComposantBase build(String key, String nom)
    {
        return new ComposantBase(key, nom);
    }

    @Override
    public String getMapIndex()
    {
        return getKey() + getBranche();
    }

    /**
     * Calcul la lettre de l'indicateur de sécurité depuis la valeur récupérée depuis le webservice Sonar.
     * 
     * @param securityRating
     *                       Valeur retournée par le webservice (String d'un long).
     */
    public void setSecurityRatingDepuisSonar(String securityRating)
    {
        switch (getString(securityRating))
        {
            case "0.0":
                setSecurityRating("A");
                break;

            case "1.0":
                setSecurityRating("A");
                break;

            case "2.0":
                setSecurityRating("B");
                break;

            case "3.0":
                setSecurityRating("C");
                break;

            case "4.0":
                setSecurityRating("D");
                break;

            case "5.0":
                setSecurityRating("E");
                break;

            case "6.0":
                setSecurityRating("F");
                break;

            default:
                setSecurityRating("F");
        }
    }

    /**
     * test si le composant à au moins un défaut critique de sécurité.
     * 
     * @return
     *         Vrai s'il y en a au moins 1.
     */
    public boolean isSecurite()
    {
        return "E".equals(getSecurityRating()) || "F".equals(getSecurityRating());
    }

    /**
     * Test si la version du composant n'est pas antérieure à la version dans SonarQube.
     * 
     * @return
     *         Vrai si la version n'est pas antérieure.
     */
    public boolean isVersionOK()
    {
        if (getVersion().isEmpty() || getVersionMax().isEmpty())
            return true;
        return getVersion().replace("-SNAPSHOT", Statics.EMPTY).compareTo(getVersionMax().replace("-SNAPSHOT", Statics.EMPTY)) >= 0;
    }

    /**
     * Calcul le liens vers SonarQube du composant depuis les paramètres de l'application.
     * 
     * @return
     *         Le liens sous forme de chaîne de caractères.
     */
    public String getLiens()
    {
        String liensBrut = null;
        if (instance == InstanceSonar.LEGACY)
            liensBrut = Statics.proprietesXML.getMapParams().get(Param.URLSONAR);
        else
            liensBrut = Statics.proprietesXML.getMapParams().get(Param.URLSONARMC);

        liensBrut += Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS) + getKey();
        return Utilities.stringToUrl(liensBrut);
    }

    /**
     * Test si un composant est au nouveau format sans l'indicateur de version.
     * 
     * @param compo
     *              Composant à tester
     * @return
     *         vrai si le composant nouveau, c'est-à-dire que son nom de finit pas par un nombre.
     */
    public boolean estNouveau()
    {
        return !PATTERNNUMBERNOMPO.matcher(getNom()).matches();
    }

    /**
     * Mise à jour de la date de repack du composant depuis une liste de repacks.
     * 
     * 
     * @param compo
     *              Composant à traiter.
     */
    public LocalDate majDateRepack(Iterable<RepackREST> repacks)
    {
        // Récuperation de la date de chaque repack pour garder la plus récente de toute
        LocalDate date = Statics.DATEINCONNUE;
        for (RepackREST repackREST : repacks)
        {
            if (!"BOREAL_Packaging".equals(repackREST.getNomGc()))
            {
                LocalDate temp = repackREST.calculDateRepack();
                if (temp.isAfter(date))
                    date = temp;
            }
        }

        // Mise à jour si on a trouvé une date.
        if (date != Statics.DATEINCONNUE)
            setDateRepack(date);

        return getDateRepack();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(branche, instance, key, matiere, nom);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        ComposantBase other = (ComposantBase) obj;
        return idBase == other.idBase 
                && Objects.equals(appli, other.appli) 
                && Objects.equals(branche, other.branche) 
                && controleAppli == other.controleAppli
                && Objects.equals(dateRepack, other.dateRepack) 
                && Objects.equals(derniereAnalyse, other.derniereAnalyse) 
                && doublon == other.doublon 
                && instance == other.instance
                && Objects.equals(key, other.key) 
                && ldc == other.ldc 
                && Objects.equals(lotRTC, other.lotRTC) 
                && matiere == other.matiere 
                && Objects.equals(nom, other.nom)
                && qualityGate == other.qualityGate 
                && Objects.equals(securityRating, other.securityRating) 
                && Objects.equals(solution, other.solution) 
                && Objects.equals(version, other.version)
                && Objects.equals(versionMax, other.versionMax);
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        if (lotRTC != null)
            this.lotRTC = lotRTC;
    }

    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    public String getBranche()
    {
        return getString(branche);
    }

    public void setBranche(String branche)
    {
        this.branche = getString(branche);
    }

    public Application getAppli()
    {
        return appli;
    }

    public void setAppli(Application appli)
    {
        if (appli != null)
            this.appli = appli;
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
     *            Valeur String du nombre de lignes.
     */
    public void setLdc(String ldc)
    {
        this.ldc = Integer.parseInt(ldc);
    }

    public String getSecurityRating()
    {
        return getString(securityRating);
    }

    public void setSecurityRating(String securityRating)
    {
        this.securityRating = getString(securityRating);
    }

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
     * Seter du QualityGate depuis une chaîne de caratères.
     * 
     * @param qualityGate
     *                    QualityGate String.
     */
    public void setQualityGate(String qualityGate)
    {
        this.qualityGate = QG.from(qualityGate);
    }

    public Matiere getMatiere()
    {
        if (matiere == null)
            matiere = Matiere.INCONNUE;
        return matiere;
    }

    public void setMatiere(Matiere matiere)
    {
        if (matiere != null)
            this.matiere = matiere;
    }

    public InstanceSonar getInstance()
    {
        if (instance == null)
            instance = InstanceSonar.LEGACY;
        return instance;
    }

    public void setInstance(InstanceSonar instance)
    {
        if (instance != null)
            this.instance = instance;
    }

    public String getVersion()
    {
        return getString(version);
    }

    public void setVersion(String version)
    {

        this.version = getString(version);
    }

    public String getVersionMax()
    {
        return getString(versionMax);
    }

    public void setVersionMax(String versionMax)
    {
        this.versionMax = getString(versionMax);
    }

    @XmlElement
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

    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDerniereAnalyse()
    {
        return derniereAnalyse;
    }

    public void setDerniereAnalyse(LocalDateTime derniereAnalyse)
    {
        if (derniereAnalyse != null)
            this.derniereAnalyse = derniereAnalyse;
    }

    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDateLeakPeriod()
    {
        return dateLeakPeriod;
    }

    public void setDateLeakPeriod(LocalDateTime dateLeakPeriod)
    {
        if (dateLeakPeriod != null)
            this.dateLeakPeriod = dateLeakPeriod;
    }

    public Solution getSolution()
    {
        return solution;
    }

    public void setSolution(Solution solution)
    {
        this.solution = solution;
        if (!solution.getListeComposants().contains(this))
            solution.getListeComposants().add(this);
    }

    public boolean isDoublon()
    {
        return doublon;
    }

    public void setDoublon(boolean doublon)
    {
        this.doublon = doublon;
    }

    public boolean isControleAppli()
    {
        return controleAppli;
    }

    public void setControleAppli(boolean controleAppli)
    {
        this.controleAppli = controleAppli;
    }

    public boolean isPropre()
    {
        return propre;
    }

    public void setPropre(boolean propre)
    {
        this.propre = propre;
    }
}

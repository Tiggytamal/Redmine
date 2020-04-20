package model.bdd;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import dao.AbstractMySQLDao;
import model.enums.EtatAnoRTC;
import model.enums.EtatAnoRTCProduit;
import model.enums.EtatCodeAppli;
import model.enums.EtatDefaut;
import model.enums.Param;
import model.enums.QG;
import model.enums.TypeDefautQualite;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe de modele qui correspond aux défaut Qualité en base de données.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity(name = "DefautQualite")
@Access(AccessType.FIELD)
@Table(name = "defauts_qualite")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DefautQualite" + AbstractMySQLDao.FINDALL, query="SELECT distinct(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.compo c "
                + "JOIN FETCH dq.lotRTC l"),
        @NamedQuery(name="DefautQualite.findAllByInstance", query="SELECT distinct(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.compo c "
                + "JOIN FETCH dq.lotRTC l "
                + "WHERE :instance = dq.compo.instance"),
        @NamedQuery(name="DefautQualite" + AbstractMySQLDao.FINDINDEX, query="SELECT dq FROM DefautQualite dq WHERE :index = dq.mapIndex"),
        @NamedQuery(name="DefautQualite.crees", query="SELECT COUNT(dq) FROM DefautQualite dq WHERE dq.dateCreation BETWEEN :debut AND :fin"),
        @NamedQuery(name="DefautQualite.abandonnees", query="SELECT COUNT(dq) FROM DefautQualite dq WHERE dq.dateCreation BETWEEN :debut AND :fin "
                + "AND dq.etatDefaut = model.enums.EtatDefaut.ABANDONNE "
                + "AND dq.etatAnoRTC IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée')"),
        
        @NamedQuery(name="DefautQualite.abandonneesSecu", query="SELECT COUNT(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.compo c "
                + "WHERE dq.dateCreation BETWEEN :debut AND :fin "
                + "AND dq.etatDefaut = model.enums.EtatDefaut.ABANDONNE "
                + "AND dq.etatAnoRTC IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée') "
                + "AND c.securityRating in ('D','E')"),
        
        @NamedQuery(name="DefautQualite.obsoletes", query="SELECT COUNT(dq) FROM DefautQualite dq WHERE dq.dateCreation BETWEEN :debut AND :fin "
                + "AND dq.etatDefaut = model.enums.EtatDefaut.OBSOLETE "
                + "AND dq.etatAnoRTC IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée')"),
        
        @NamedQuery(name="DefautQualite.obsoletesSecu", query="SELECT COUNT(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.compo c "
                + "WHERE dq.dateCreation BETWEEN :debut AND :fin "
                + "AND dq.etatDefaut = model.enums.EtatDefaut.OBSOLETE "
                + "AND dq.etatAnoRTC IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée') "
                + "AND c.securityRating in ('D','E')"),
        
        @NamedQuery(name="DefautQualite.closes", query="SELECT COUNT(dq) FROM DefautQualite dq WHERE dq.dateCreation BETWEEN :debut AND :fin  "
                + "AND dq.etatDefaut = model.enums.EtatDefaut.CLOS "
                + "AND dq.etatAnoRTC IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée')"),
        
        @NamedQuery(name="DefautQualite.closesSecu", query="SELECT COUNT(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.compo c "
                + "WHERE dq.dateCreation BETWEEN :debut AND :fin  "
                + "AND dq.etatDefaut = model.enums.EtatDefaut.CLOS "
                + "AND dq.etatAnoRTC IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée') "
                + "AND c.securityRating in ('D','E')"),
        
        @NamedQuery(name="DefautQualite.enCours", query="SELECT COUNT(dq) FROM DefautQualite dq WHERE dq.dateCreation BETWEEN :debut AND :fin "
                + "AND dq.etatAnoRTC NOT IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée')"),
        
        @NamedQuery(name="DefautQualite.enCoursSecu", query="SELECT COUNT(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.compo c "
                + "WHERE dq.dateCreation BETWEEN :debut AND :fin "
                + "AND dq.etatAnoRTC NOT IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée') "
                + "AND c.securityRating in ('D','E')"),
        
        @NamedQuery(name="DefautQualite.enCoursTotal", query="SELECT COUNT(dq) FROM DefautQualite dq WHERE dq.etatAnoRTC NOT IN ('Close', '9.Validée', 'Rejetée', 'Résolue', 'Abandonnée')"),
        @NamedQuery(name="DefautQualite" + AbstractMySQLDao.RESET, query="DELETE FROM DefautQualite")
})
//@formatter:on
public class DefautQualite extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = ComposantBase.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "composant")
    private ComposantBase compo;

    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = LotRTC.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "lotRTC")
    private LotRTC lotRTC;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "defauts_utilisateurs", joinColumns = @JoinColumn(name = "defaut"), inverseJoinColumns = @JoinColumn(name = "utilisateur"))
    private Set<Utilisateur> createurIssues;

    @Column(name = "index_map", nullable = false)
    private String mapIndex;

    @Column(name = "analyseId", nullable = true, length = 64)
    private String analyseId;

    @Column(name = "numero_anoRTC", nullable = true, length = 6)
    private int numeroAnoRTC;

    @Column(name = "etatRTC", nullable = true, length = 40)
    private String etatAnoRTC;

    @Lob
    @Column(name = "remarque", nullable = true)
    private String remarque;

    @Column(name = "date_detection", nullable = false)
    private LocalDate dateDetection;

    @Column(name = "date_creation", nullable = true)
    private LocalDate dateCreation;

    @Column(name = "date_relance", nullable = true)
    private LocalDate dateRelance;

    @Column(name = "date_resolution", nullable = true)
    private LocalDate dateReso;

    @Column(name = "date_reouverture", nullable = true)
    private LocalDate dateReouv;

    @Column(name = "date_mep_prev", nullable = true)
    private LocalDate dateMepPrev;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_defaut", nullable = false)
    private EtatDefaut etatDefaut;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_code_appli", nullable = true)
    private EtatCodeAppli etatCodeAppli;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_defaut", nullable = false)
    private TypeDefautQualite typeDefaut;

    /*---------- CONSTRUCTEURS ----------*/

    DefautQualite()
    {
        etatDefaut = EtatDefaut.NOUVEAU;
        remarque = Statics.EMPTY;
        dateDetection = LocalDate.now();
        typeDefaut = TypeDefautQualite.INCONNU;
        createurIssues = new HashSet<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int hashCode()
    {
        return Objects.hash(idBase, analyseId, compo, createurIssues, dateCreation, dateDetection, dateMepPrev, dateRelance, dateReouv, dateReso, etatCodeAppli, etatDefaut, etatAnoRTC, lotRTC,
                numeroAnoRTC, remarque, typeDefaut);
    }
    
    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        DefautQualite other = (DefautQualite) obj;
        return idBase == other.idBase 
                && Objects.equals(analyseId, other.analyseId) 
                && Objects.equals(compo, other.compo) 
                && Objects.equals(createurIssues, other.createurIssues)
                && Objects.equals(dateCreation, other.dateCreation) 
                && Objects.equals(dateDetection, other.dateDetection) 
                && Objects.equals(dateMepPrev, other.dateMepPrev)
                && Objects.equals(dateRelance, other.dateRelance) 
                && Objects.equals(dateReouv, other.dateReouv) 
                && Objects.equals(dateReso, other.dateReso) 
                && etatCodeAppli == other.etatCodeAppli
                && etatDefaut == other.etatDefaut 
                && Objects.equals(etatAnoRTC, other.etatAnoRTC) 
                && Objects.equals(lotRTC, other.lotRTC) 
                && numeroAnoRTC == other.numeroAnoRTC
                && Objects.equals(remarque, other.remarque) 
                && typeDefaut == other.typeDefaut;
        //@formatter:on
    }
    
    // L'index est calcule en prenant la clef du compo + la branche + le numero de lot.
    @Override
    public String getMapIndex()
    {
        return mapIndex;
    }

    /**
     * Permet de vérifier si une anomalie a été traitee ou non. C'est-à-dire si il y a un numero d'anomalie ou un commentaire.
     * 
     * @return
     *         Vrai si l'anomalie a été traitée.
     */
    public boolean calculTraitee()
    {
        if ((!getRemarque().isEmpty() || numeroAnoRTC != 0) && etatDefaut == EtatDefaut.NOUVEAU)
            etatDefaut = EtatDefaut.ENCOURS;
        return etatDefaut != EtatDefaut.NOUVEAU;
    }
    
    public boolean calculDefautEnCours()
    {
        // Liste des état terminés
        List<String> listeEtatsFermes = new ArrayList<>();
        listeEtatsFermes.add(EtatAnoRTC.ABANDONNEE.getValeur());
        listeEtatsFermes.add(EtatAnoRTC.CLOSE.getValeur());
        listeEtatsFermes.add(EtatAnoRTC.REJETEE.getValeur());
        listeEtatsFermes.add(EtatAnoRTCProduit.ABANDONNEE.getValeur());
        listeEtatsFermes.add(EtatAnoRTCProduit.CLOSE.getValeur());
        listeEtatsFermes.add(EtatAnoRTCProduit.REJETE.getValeur());

        // On affiche le défaut si l'état de l'anomalie RTC n'est pas fermée
        if (getNumeroAnoRTC() != 0 && !listeEtatsFermes.contains(getEtatAnoRTC()))
            return true;

        // On n'affiche pas les anomalies abandonnées
        if (getEtatDefaut() == EtatDefaut.ABANDONNE || getEtatDefaut() == EtatDefaut.OBSOLETE)
            return false;

        // On affiche pas les anomalies closes sauf si le QualityGate est en erreur
        return (getEtatDefaut() != EtatDefaut.CLOS || (getEtatDefaut() == EtatDefaut.CLOS && getCompo().getQualityGate() == QG.ERROR));
    }

    /**
     * Initialise l'index de la map depuis le numéro de lot RTC et l'index du composant.
     */
    public void initMapIndex()
    {
        if (compo != null && lotRTC != null)
            mapIndex = compo.getMapIndex() + lotRTC.getNumero();
    }

    /**
     * Calcul le liens vers l'anomalie RTC
     * 
     * @return
     *         Le lien.
     */
    public String getLiensAno()
    {
        if (numeroAnoRTC != 0 && getLotRTC() != null && !getLotRTC().getProjetRTC().isEmpty())
        {
            String liensBrut = Statics.proprietesXML.getMapParams().get(Param.LIENSRTC) + getLotRTC().getProjetRTC() + Statics.FINLIENSRTC + String.valueOf(numeroAnoRTC);
            return Utilities.stringToUrl(liensBrut);
        }
        return Statics.EMPTY;
    }

    /**
     * Ajoute un utilisateur dans la liste des personne responsables de la création de défaut qualimétirques dans SonarQube pour ce composant.
     * 
     * @param user
     *             L'utilisateur à rajouter.
     */
    public void ajouterCreateurIssue(Utilisateur user)
    {
        getCreateurIssues().add(user);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public ComposantBase getCompo()
    {
        return compo;
    }

    public void setCompo(ComposantBase compo)
    {
        if (compo != null)
        {
            this.compo = compo;
            initMapIndex();
        }
    }

    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        if (lotRTC != null)
        {
            this.lotRTC = lotRTC;
            initMapIndex();
        }
    }

    public int getNumeroAnoRTC()
    {
        return numeroAnoRTC;
    }

    public void setNumeroAnoRTC(int numeroAnoRTC)
    {
        if (numeroAnoRTC != 0)
            this.numeroAnoRTC = numeroAnoRTC;
    }

    public Set<Utilisateur> getCreateurIssues()
    {
        if (createurIssues == null)
            createurIssues = new HashSet<>();
        return createurIssues;
    }

    public void setCreateurIssues(Set<Utilisateur> createurIssues)
    {
        if (createurIssues != null && !createurIssues.isEmpty())
            this.createurIssues = createurIssues;
    }

    public String getAnalyseId()
    {
        return getString(analyseId);
    }

    public void setAnalyseId(String analyseId)
    {
        if (analyseId != null && !analyseId.isEmpty())
            this.analyseId = analyseId;
    }

    public String getEtatAnoRTC()
    {
        return getString(etatAnoRTC);
    }

    public void setEtatAnoRTC(String etatAnoRTC)
    {
        this.etatAnoRTC = getString(etatAnoRTC).trim();
    }

    public String getRemarque()
    {
        return getString(remarque);
    }

    public void setRemarque(String remarque)
    {
        this.remarque = getString(remarque);
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation)
    {
        if (dateCreation != null)
            this.dateCreation = dateCreation;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateDetection()
    {
        if (dateDetection == null)
            dateDetection = LocalDate.now();
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection)
    {
        if (dateDetection != null)
            this.dateDetection = dateDetection;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateRelance()
    {
        return dateRelance;
    }

    public void setDateRelance(LocalDate dateRelance)
    {
        this.dateRelance = dateRelance;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateReso()
    {
        return dateReso;
    }

    public void setDateReso(LocalDate dateReso)
    {
        if (dateReso != null)
            this.dateReso = dateReso;
    }

    public EtatDefaut getEtatDefaut()
    {
        if (etatDefaut == null)
            etatDefaut = EtatDefaut.NOUVEAU;
        return etatDefaut;
    }

    public void setEtatDefaut(EtatDefaut etatDefaut)
    {
        if (etatDefaut == null)
            throw new TechnicalException("Tentative de mise à null de DefautQualite.etatDefaut");
        this.etatDefaut = etatDefaut;
    }

    public EtatCodeAppli getEtatCodeAppli()
    {
        if (etatCodeAppli == null)
            etatCodeAppli = EtatCodeAppli.NA;
        return etatCodeAppli;
    }

    public void setEtatCodeAppli(EtatCodeAppli etatCodeAppli)
    {
        if (etatCodeAppli == null)
            throw new TechnicalException("Tentative de mise à null de DefautQualite.etatCodeAppli");
        this.etatCodeAppli = etatCodeAppli;
    }

    public TypeDefautQualite getTypeDefaut()
    {
        if (typeDefaut == null)
            typeDefaut = TypeDefautQualite.INCONNU;
        return typeDefaut;
    }

    public void setTypeDefaut(TypeDefautQualite typeDefault)
    {
        if (typeDefault == null)
            throw new TechnicalException("Tentative de mise à null de DefautQualite.typeDefaut");
        this.typeDefaut = typeDefault;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateReouv()
    {
        return dateReouv;
    }

    public void setDateReouv(LocalDate dateReouv)
    {
        if (dateReouv != null)
            this.dateReouv = dateReouv;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateMepPrev()
    {
        return dateMepPrev;
    }

    public void setDateMepPrev(LocalDate dateMepPrev)
    {
        if (dateMepPrev != null)
            this.dateMepPrev = dateMepPrev;
    }
}

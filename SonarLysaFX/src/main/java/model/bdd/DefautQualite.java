package model.bdd;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import dao.AbstractDao;
import model.enums.EtatDefaut;
import model.enums.Param;
import model.enums.TypeAction;
import model.enums.TypeDefaut;
import model.enums.TypeVersion;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe de modèle qui correspond aux données du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "defauts_qualite")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DefautQualite" + AbstractDao.FINDALL, query="SELECT distinct(dq) FROM DefautQualite dq "
                + "JOIN FETCH dq.lotRTC l "
                + "LEFT JOIN FETCH dq.defautsAppli da "),
        @NamedQuery(name="DefautQualite" + AbstractDao.FINDINDEX, query="SELECT dq FROM DefautQualite dq WHERE dq.lotRTC.lot = :index"),
        @NamedQuery(name="DefautQualite" + AbstractDao.RESET, query="DELETE FROM DefautQualite")
})
//@formatter:on
@XmlRootElement
public class DefautQualite extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    
    /** logger plantage */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne(targetEntity = LotRTC.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "lotRTC")
    private LotRTC lotRTC;

    @Column(name = "liens_lot", nullable = true)
    private String liensLot;

    @Column(name = "numero_anoRTC", nullable = true, length = 6)
    private int numeroAnoRTC;

    @Column(name = "liens_ano", nullable = true)
    private String liensAno;

    @Column(name = "etatRTC", nullable = true, length = 40)
    private String etatRTC;

    @Column(name = "securite", nullable = false)
    private boolean securite;

    @Column(name = "remarque", nullable = true)
    private String remarque;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_version", nullable = false)
    private TypeVersion typeVersion;

    @Column(name = "date_creation", nullable = true)
    private LocalDate dateCreation;

    @Column(name = "date_detection", nullable = false)
    private LocalDate dateDetection;

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
    @Column(name = "action", nullable = false)
    private TypeAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_defaut", nullable = false)
    private TypeDefaut typeDefaut;

    @OneToMany(cascade = CascadeType.MERGE, targetEntity = DefautAppli.class, mappedBy = "defautQualite")
    private Set<DefautAppli> defautsAppli;

    @Transient
    private String newCodeAppli;

    /*---------- CONSTRUCTEURS ----------*/

    DefautQualite()
    {
        typeVersion = TypeVersion.SNAPSHOT;
        etatDefaut = EtatDefaut.NOUVEAU;
        action = TypeAction.VIDE;
        remarque = Statics.EMPTY;
        dateDetection = LocalDate.now();
        typeDefaut = TypeDefaut.INCONNU;
        defautsAppli = new HashSet<>();
    }

    DefautQualite(LotRTC lotRTC)
    {
        this();
        this.lotRTC = lotRTC;
        creerLiensLotRTC();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static DefautQualite build(LotRTC lotRTC)
    {
        return new DefautQualite(lotRTC);
    }

    @Override
    public String getMapIndex()
    {
        return getLotRTC().getLot();
    }

    /**
     * Permet de vérifier si une anomalie a été traitée ou non. C'est-à-dire si il y a un numéro d'anomalie ou un commentaire.
     * 
     * @return
     */
    public boolean calculTraitee()
    {
        if ((!getRemarque().isEmpty() || numeroAnoRTC != 0 || action != TypeAction.VIDE) && etatDefaut == EtatDefaut.NOUVEAU)
            etatDefaut = EtatDefaut.TRAITE;
        return etatDefaut != EtatDefaut.NOUVEAU;
    }

    public void controleLiens()
    {
        controleLiensAno();
        controleLiensLot();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création du liens vers l'anomalie dans RTC
     */
    private void creerLiensAnoRTC()
    {
        if (getLotRTC() != null && !getLotRTC().getProjetRTC().isEmpty())
        {
            String urlStr = Statics.proprietesXML.getMapParams().get(Param.LIENSANOS) + getLotRTC().getProjetRTC() + Statics.FINLIENSANO + String.valueOf(numeroAnoRTC);
            URL url;
            try
            {
                // Encoding de l'url en format compréhensible pour le navigateur.
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                liensAno = uri.toASCIIString();
            }
            catch (MalformedURLException | URISyntaxException e)
            {
                LOGPLANTAGE.error(e);
            }
        }
        
    }

    /**
     * Création du liens vers le lot dans RTC
     */
    private void creerLiensLotRTC()
    {
        if (getLotRTC() != null)
            liensLot = Statics.proprietesXML.getMapParams().get(Param.LIENSLOTS) + getLotRTC().getLot();
    }

    private void controleLiensAno()
    {
        if (numeroAnoRTC != 0 && (liensAno == null || liensAno.isEmpty()))
            creerLiensAnoRTC();
    }

    private void controleLiensLot()
    {
        if (liensLot == null || liensLot.isEmpty())
            creerLiensLotRTC();
    }

    /*---------- ACCESSEURS ----------*/

    @XmlElement(name = "lotRTC")
    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        if (lotRTC != null)
        {
            this.lotRTC = lotRTC;
            creerLiensLotRTC();
        }
    }

    @XmlAttribute(name = "numeroAnoRTC")
    public int getNumeroAnoRTC()
    {
        return numeroAnoRTC;
    }

    public void setNumeroAnoRTC(int numeroAnoRTC)
    {
        if (numeroAnoRTC != 0)
        {
            this.numeroAnoRTC = numeroAnoRTC;
            creerLiensAnoRTC();
        }
    }

    @XmlAttribute(name = "etatRTC")
    public String getEtatRTC()
    {
        return getString(etatRTC);
    }

    public void setEtatRTC(String etatRTC)
    {
        this.etatRTC = getString(etatRTC).trim();
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

    @XmlAttribute(name = "remarque")
    public String getRemarque()
    {
        return getString(remarque);
    }

    public void setRemarque(String remarque)
    {
        this.remarque = getString(remarque);
    }

    @XmlAttribute(name = "liensLot")
    public String getLiensLot()
    {
        controleLiensLot();
        return liensLot;
    }

    public void setLiensLot(String liensLot)
    {
        this.liensLot = liensLot;
    }

    @XmlAttribute(name = "liensAno")
    public String getLiensAno()
    {
        controleLiensAno();
        return getString(liensAno);
    }

    public void setLiensAno(String liensAno)
    {
        this.liensAno = getString(liensAno);
    }

    @XmlAttribute(name = "typeVersion")
    public TypeVersion getTypeVersion()
    {
        if (typeVersion == null)
            typeVersion = TypeVersion.SNAPSHOT;
        return typeVersion;
    }

    public void setTypeVersion(TypeVersion typeVersion)
    {
        if (typeVersion != null)
            this.typeVersion = typeVersion;
    }

    @XmlAttribute(name = "dateCreation")
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

    @XmlAttribute(name = "dateDetection")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateDetection()
    {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection)
    {
        if (dateDetection != null)
            this.dateDetection = dateDetection;
    }

    @XmlAttribute(name = "dateRelance")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateRelance()
    {
        return dateRelance;
    }

    public void setDateRelance(LocalDate dateRelance)
    {
        this.dateRelance = dateRelance;
    }

    @XmlAttribute(name = "dateReso")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateReso()
    {
        return dateReso;
    }

    public void setDateReso(LocalDate dateReso)
    {
        this.dateReso = dateReso;
    }

    @XmlAttribute(name = "action")
    public TypeAction getAction()
    {
        if (action == null)
            action = TypeAction.VIDE;
        return action;
    }

    public void setAction(TypeAction action)
    {
        if (action != null)
            this.action = action;
    }

    @XmlAttribute(name = "etatDefaut")
    public EtatDefaut getEtatDefaut()
    {
        return etatDefaut;
    }

    public void setEtatDefaut(EtatDefaut etatDefaut)
    {
        if (etatDefaut == null)
            throw new TechnicalException("Tentative de mise à null de DefautQualite.etatDefaut");
        this.etatDefaut = etatDefaut;
    }

    @XmlAttribute(name = "typeDefaut")
    public TypeDefaut getTypeDefaut()
    {
        return typeDefaut;
    }

    public void setTypeDefaut(TypeDefaut typeDefault)
    {
        if (typeDefault == null)
            throw new TechnicalException("Tentative de mise à null de DefautQualite.typeDefaut");
        this.typeDefaut = typeDefault;
    }

    @XmlTransient
    public String getNewCodeAppli()
    {
        return getString(newCodeAppli);
    }

    public void setNewCodeAppli(String newCodeAppli)
    {
        this.newCodeAppli = getString(newCodeAppli);
    }

    @XmlAttribute(name = "dateReouv")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateReouv()
    {
        return dateReouv;
    }

    public void setDateReouv(LocalDate dateReouv)
    {
        this.dateReouv = dateReouv;
    }

    @XmlAttribute(name = "dateMepPrev")
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

    @XmlElementWrapper
    @XmlElement(name = "defautsAppli")
    public Set<DefautAppli> getDefautsAppli()
    {
        if (defautsAppli == null)
            defautsAppli = new HashSet<>();
        return defautsAppli;
    }

    public void setDefautsAppli(Set<DefautAppli> defautsAppli)
    {
        if (defautsAppli != null && !defautsAppli.isEmpty())
            this.defautsAppli = defautsAppli;
    }
}

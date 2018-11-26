package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
        @NamedQuery(name="DefautQualite" + AbstractDao.FINDALL, query="SELECT dq FROM DefautQualite dq LEFT JOIN FETCH dq.lotRTC l"),
        @NamedQuery(name="DefautQualite" + AbstractDao.FINDINDEX, query="SELECT dq FROM DefautQualite dq WHERE dq.lotRTC.lot = :index"),
        @NamedQuery(name="DefautQualite" + AbstractDao.RESET, query="DELETE FROM DefautQualite")
})
//@formatter:on
@XmlRootElement
public class DefautQualite extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

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

    @Column(name = "etatRTC", nullable = true, length = 32)
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

    @Transient
    private String nomCompoAppli;

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
        typeDefaut = TypeDefaut.SONAR;
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
        if (getLotRTC() != null && getLotRTC().getProjetRTC() != null)
            liensAno = Statics.proprietesXML.getMapParams().get(Param.LIENSANOS) + getLotRTC().getProjetRTC().replace(Statics.SPACE, "%20") + Statics.FINLIENSANO + String.valueOf(numeroAnoRTC);
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
        this.lotRTC = lotRTC;
        creerLiensLotRTC();
    }

    @XmlAttribute(name = "numeroAnoRTC")
    public int getNumeroAnoRTC()
    {
        return numeroAnoRTC;
    }

    public void setNumeroAnoRTC(int numeroAnoRTC)
    {
        this.numeroAnoRTC = numeroAnoRTC;
        if (numeroAnoRTC != 0)
            creerLiensAnoRTC();
    }

    @XmlAttribute(name = "etatRTC")
    public String getEtatRTC()
    {
        return getString(etatRTC);
    }

    public void setEtatRTC(String etatRTC)
    {
        this.etatRTC = etatRTC.trim();
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
        this.remarque = remarque;
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
        this.liensAno = liensAno;
    }

    @XmlAttribute(name = "typeVersion")
    public TypeVersion getTypeVersion()
    {
        return typeVersion;
    }

    public void setTypeVersion(TypeVersion typeVersion)
    {
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
    public String getNomCompoAppli()
    {
        return getString(nomCompoAppli);
    }

    public void setNomCompoAppli(String nomCompoAppli)
    {
        this.nomCompoAppli = nomCompoAppli;
    }

    @XmlTransient
    public String getNewCodeAppli()
    {
        return getString(newCodeAppli);
    }

    public void setNewCodeAppli(String newCodeAppli)
    {
        this.newCodeAppli = newCodeAppli;
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
        this.dateMepPrev = dateMepPrev;
    }
}

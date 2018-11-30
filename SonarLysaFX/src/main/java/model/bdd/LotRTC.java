package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import dao.ListeDao;
import model.enums.EtatLot;
import model.enums.GroupeProduit;
import model.enums.Matiere;
import model.enums.QG;
import utilities.Statics;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe répresentant l'extraction d'un lot depuis RTC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@Entity
@Table(name = "lots_RTC")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="LotRTC.findAll", query="SELECT l FROM LotRTC l "
                + "LEFT JOIN FETCH l.projetClarity p "
                + "LEFT JOIN FETCH l.defaultQualite dq "
                + "LEFT JOIN FETCH l.compos c"),
        @NamedQuery(name="LotRTC.findByIndex", query="SELECT l FROM LotRTC l WHERE l.lot = :index"),
        @NamedQuery(name="LotRTC.resetTable", query="DELETE FROM LotRTC")
})
//@formatter:on
@XmlRootElement
public class LotRTC extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "lot", nullable = false, length = 6)
    private String lot;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne(targetEntity = ProjetClarity.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "projet_Clarity")
    private ProjetClarity projetClarity;

    // Permet d'enregistrer temporairement la donnée depuis RTC avant traitement
    @Transient
    private String projetClarityString;

    @Column(name = "cpi_projet", nullable = false, length = 128)
    private String cpiProjet;

    @ManyToOne(targetEntity = Edition.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "edition")
    private Edition edition;

    // Permet d'enregistrer temporairement la donnée depuis RTC avant traitement
    @Transient
    private String editionString;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatLot", nullable = true)
    private EtatLot etatLot;

    @Column(name = "projet_RTC", nullable = false, length = 128)
    private String projetRTC;

    @Column(name = "date_maj_etat", nullable = true)
    private LocalDate dateMajEtat;

    @Column(name = "date_repack", nullable = true)
    private LocalDate dateRepack;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualityGate", nullable = false)
    private QG qualityGate;

    @ElementCollection(targetClass = Matiere.class)
    @CollectionTable(name = "lots_matieres", joinColumns = @JoinColumn(name = "lot"))
    @Enumerated(EnumType.STRING)
    @Column(name = "matiere", nullable = true)
    private Set<Matiere> matieres;

    @Enumerated(EnumType.STRING)
    @Column(name = "groupe", nullable = true)
    private GroupeProduit groupeProduit;

    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne(targetEntity = DefautQualite.class, mappedBy = "lotRTC")
    private DefautQualite defaultQualite;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "lotRTC", targetEntity = ComposantSonar.class)
    private List<ComposantSonar> compos;

    @Column(name = "rtc_hs", nullable = false)
    private boolean rtcHS;

    /*---------- CONSTRUCTEURS ----------*/

    LotRTC()
    {
        qualityGate = QG.NONE;
        matieres = new HashSet<>();
        groupeProduit = GroupeProduit.AUCUN;
        rtcHS = false;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getLot();
    }

    public static LotRTC getLotRTCInconnu(String lot)
    {
        LotRTC retour = new LotRTC();
        retour.lot = lot;
        retour.libelle = Statics.EMPTY;
        retour.projetClarityString = Statics.EMPTY;
        retour.editionString = Statics.EMPTY;
        retour.cpiProjet = Statics.EMPTY;
        retour.etatLot = EtatLot.NOUVEAU;
        retour.projetRTC = Statics.EMPTY;
        retour.edition = ListeDao.daoEdition.recupEltParIndex(Statics.EDINCONNUE);
        return retour;
    }

    public LotRTC update(LotRTC update)
    {
        libelle = update.libelle;
        projetClarity = update.projetClarity;
        cpiProjet = update.cpiProjet;
        edition = update.edition;
        etatLot = update.etatLot;
        projetRTC = update.projetRTC;
        dateMajEtat = update.dateMajEtat;
        groupeProduit = update.groupeProduit;
        projetClarityString = update.projetClarityString;
        editionString = update.editionString;
        rtcHS = update.rtcHS;
        return this;

    }

    /**
     * Retourne la liste des matieres de l'anomalie sous forme d'une chaine de caractères enregistrable dans Excel
     * 
     * @return
     */
    @XmlTransient
    public String getMatieresString()
    {
        StringBuilder builder = new StringBuilder();
        if (matieres == null)
            return Statics.EMPTY;

        for (Iterator<Matiere> iter = matieres.iterator(); iter.hasNext();)
        {
            builder.append(iter.next().toString());
            if (iter.hasNext())
                builder.append(" - ");
        }
        return builder.toString();
    }

    public void addMatiere(Matiere matiere)
    {
        if (matieres == null)
            matieres = new HashSet<>();
        if (matiere != null)
            matieres.add(matiere);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "lot")
    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    @XmlAttribute(name = "libemme")
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    @XmlTransient
    public ProjetClarity getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(ProjetClarity projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    @XmlAttribute(name = "cpiProjet")
    public String getCpiProjet()
    {
        return getString(cpiProjet);
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }

    @XmlElement(name = "edition")
    public Edition getEdition()
    {
        return edition;
    }

    public void setEdition(Edition edition)
    {
        this.edition = edition;
    }

    @XmlAttribute(name = "etatLot")
    public EtatLot getEtatLot()
    {
        return etatLot == null ? EtatLot.INCONNU : etatLot;
    }

    public void setEtatLot(EtatLot etatLot)
    {
        this.etatLot = etatLot;
    }

    @XmlAttribute(name = "projetRTC")
    public String getProjetRTC()
    {
        return getString(projetRTC);
    }

    public void setProjetRTC(String projetRTC)
    {
        this.projetRTC = projetRTC;
    }

    @XmlAttribute(name = "dateMajEtat")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateMajEtat()
    {
        return dateMajEtat;
    }

    public void setDateMajEtat(LocalDate dateMajEtat)
    {
        this.dateMajEtat = dateMajEtat;
    }

    @XmlTransient
    public String getProjetClarityString()
    {
        return getString(projetClarityString);
    }

    public void setProjetClarityString(String projetClarityString)
    {
        this.projetClarityString = projetClarityString;
    }

    @XmlAttribute(name = "qualityGate")
    public QG getQualityGate()
    {
        return qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        if (qualityGate != null)
            this.qualityGate = qualityGate;
    }

    @XmlElementWrapper
    @XmlElement(name = "matiere")
    public Set<Matiere> getMatieres()
    {
        if (matieres == null)
            matieres = new HashSet<>();
        return matieres;
    }

    public void setMatieres(Set<Matiere> matieres)
    {
        this.matieres = matieres;
    }

    @XmlAttribute(name = "groupeProduit")
    public GroupeProduit getGroupeProduit()
    {
        return groupeProduit;
    }

    public void setGroupeProduit(GroupeProduit groupeProduit)
    {
        this.groupeProduit = groupeProduit;
    }

    @XmlTransient
    public DefautQualite getDefaultQualite()
    {
        return defaultQualite;
    }

    public void setDefaultQualite(DefautQualite defaultQualite)
    {
        this.defaultQualite = defaultQualite;
    }

    @XmlTransient
    public String getEditionString()
    {
        return editionString;
    }

    public void setEditionString(String editionString)
    {
        this.editionString = editionString;
    }

    @XmlAttribute(name = "dateRepack")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateRepack()
    {
        return dateRepack;
    }

    public void setDateRepack(LocalDate dateRepack)
    {
        this.dateRepack = dateRepack;
    }

    @XmlAttribute(name = "rtcHS")
    public boolean isRtcHS()
    {
        return rtcHS;
    }

    public void setRtcHS(boolean rtcHS)
    {
        this.rtcHS = rtcHS;
    }

    @XmlTransient
    public List<ComposantSonar> getCompos()
    {
        return compos;
    }

    public void setCompos(List<ComposantSonar> compos)
    {
        this.compos = compos;
    }
}

package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatLot;
import model.enums.GroupeProjet;
import model.enums.Matiere;
import model.enums.QG;
import utilities.Statics;

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
                + "LEFT JOIN FETCH l.defaultQualite dq"),
        @NamedQuery(name="LotRTC.findByIndex", query="SELECT l FROM LotRTC l WHERE l.lot = :index"),
        @NamedQuery(name="LotRTC.resetTable", query="DELETE FROM LotRTC")
})
//@formatter:on
public class LotRTC extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "lot", nullable = false, length = 6)
    private String lot;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = ProjetClarity.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "projet_Clarity")
    private ProjetClarity projetClarity;

    @Transient
    private String projetClarityString;

    @Column(name = "cpi_projet", nullable = false, length = 128)
    private String cpiProjet;

    @Column(name = "edition", nullable = false, length = 32)
    private String edition;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatLot", nullable = true)
    private EtatLot etatLot;

    @Column(name = "projet_RTC", nullable = false, length = 128)
    private String projetRTC;

    @Column(name = "date_maj_etat", nullable = true)
    private LocalDate dateMajEtat;

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
    private GroupeProjet groupe;
        
    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne (targetEntity = DefautQualite.class, mappedBy = "lotRTC")
    private DefautQualite defaultQualite;

    /*---------- CONSTRUCTEURS ----------*/

    LotRTC()
    {
        qualityGate = QG.NONE;
        matieres = new HashSet<>();
        groupe = GroupeProjet.AUCUN;
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
        retour.cpiProjet = Statics.EMPTY;
        retour.etatLot = EtatLot.NOUVEAU;
        retour.projetRTC = Statics.EMPTY;
        retour.edition = Statics.EMPTY;
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
        groupe = update.groupe;
        return this;

    }

    /**
     * Retourne la liste des matieres de l'anomalie sous forme d'une chaine de caractères enregistrable dans Excel
     * 
     * @return
     */
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

    /**
     * Remplie la liste des matières depuis une chaine de caractères. Cahque matière doit être séparées par un "-".
     * 
     */
    public void setMatieresString(String matieresString)
    {
        if (matieresString == null || matieresString.isEmpty())
            return;
        if (matieres == null)
            matieres = new HashSet<>();
        else
            matieres.clear();
        for (String matiere : matieresString.split("-"))
        {
            matieres.add(Matiere.from(matiere.trim()));
        }
    }

    public void addMatiere(Matiere matiere)
    {
        matieres.add(matiere);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    public ProjetClarity getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(ProjetClarity projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    public String getCpiProjet()
    {
        return getString(cpiProjet);
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    public EtatLot getEtatLot()
    {
        return etatLot == null ? EtatLot.INCONNU : etatLot;
    }

    public void setEtatLot(EtatLot etatLot)
    {
        this.etatLot = etatLot;
    }

    public String getProjetRTC()
    {
        return getString(projetRTC);
    }

    public void setProjetRTC(String projetRTC)
    {
        this.projetRTC = projetRTC;
    }

    public LocalDate getDateMajEtat()
    {
        return dateMajEtat;
    }

    public void setDateMajEtat(LocalDate dateMajEtat)
    {
        this.dateMajEtat = dateMajEtat;
    }

    public String getProjetClarityString()
    {
        return projetClarityString;
    }

    public void setProjetClarityString(String projetClarityString)
    {
        this.projetClarityString = projetClarityString;
    }

    public QG getQualityGate()
    {
        return qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        this.qualityGate = qualityGate;
    }

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

    public GroupeProjet getGroupe()
    {
        return groupe;
    }

    public void setGroupe(GroupeProjet groupe)
    {
        this.groupe = groupe;
    }

    public DefautQualite getDefaultQualite()
    {
        return defaultQualite;
    }

    public void setDefaultQualite(DefautQualite defaultQualite)
    {
        this.defaultQualite = defaultQualite;
    }
}

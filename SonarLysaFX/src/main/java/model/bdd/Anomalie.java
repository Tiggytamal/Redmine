package model.bdd;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.GroupeComposant;
import model.enums.Matiere;
import model.enums.TypeAction;
import model.enums.TypeVersion;

/**
 * Classe de modèle qui correspond aux données du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "anomalies")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Anomalie.findAll", query="SELECT a FROM Anomalie a "
                + "JOIN FETCH a.lotRTC l"),
        @NamedQuery(name="Anomalie.findByindex", query="SELECT a FROM Anomalie a WHERE a.lotRTC.lot = :index"),
        @NamedQuery(name="Anomalie.resetTable", query="DELETE FROM Anomalie")
})
//@formatter:on
public class Anomalie extends AbstractBDDModele
{
    /*---------- ATTRIBUTS ----------*/

    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = LotRTC.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "lotRTC")
    private LotRTC lotRTC;

    @Column(name = "liens_lot", nullable = true)
    private String liensLot;

    @Column(name = "numero_anomalie", nullable = true, length = 6)
    private int numeroAnomalie;

    @Column(name = "liens_ano", nullable = true)
    private String liensAno;

    @Column(name = "etat", nullable = true, length = 32)
    private String etat;

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

    @Transient
    private boolean traitee;

    @ElementCollection(targetClass = Matiere.class)
    @CollectionTable(name = "anomalies_matieres", joinColumns = @JoinColumn(name = "anomalie"))
    @Enumerated(EnumType.STRING)
    @Column(name = "matiere", nullable = true)
    private Set<Matiere> matieres;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = true)
    private TypeAction action;

    @Column(name = "groupe", nullable = false)
    private GroupeComposant groupe;

    /*---------- CONSTRUCTEURS ----------*/

    Anomalie()
    {
        matieres = new HashSet<>();
    }

    Anomalie(LotRTC lotRTC)
    {
        this();
        this.lotRTC = lotRTC;
    }

    /*---------- METHODES PUBLIQUES ----------*/

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
        traitee = (!getRemarque().isEmpty()) || (numeroAnomalie != 0);
        return traitee;
    }

    /**
     * Retourne la liste des matieres de l'anomalie sous forme d'une chaine de caractères enregistrable dans Excel
     * 
     * @return
     */
    public String getMatieresString()
    {
        StringBuilder builder = new StringBuilder();

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
        matieres.clear();
        for (String matiere : matieresString.split("-"))
        {
            matieres.add(Matiere.from(matiere.trim()));
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/

    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        this.lotRTC = lotRTC;
    }

    public int getNumeroAnomalie()
    {
        return numeroAnomalie;
    }

    public void setNumeroAnomalie(int numeroAnomalie)
    {
        this.numeroAnomalie = numeroAnomalie;
    }

    public String getEtat()
    {
        return getString(etat);
    }

    public void setEtat(String etat)
    {
        this.etat = etat;
    }

    public boolean isSecurite()
    {
        return securite;
    }

    public void setSecurite(boolean securite)
    {
        this.securite = securite;
    }

    public String getRemarque()
    {
        return getString(remarque);
    }

    public void setRemarque(String remarque)
    {
        this.remarque = remarque;
    }

    public String getLiensLot()
    {
        return getString(liensLot);
    }

    public void setLiensLot(String liensLot)
    {
        this.liensLot = liensLot;
    }

    public String getLiensAno()
    {
        return getString(liensAno);
    }

    public void setLiensAno(String liensAno)
    {
        this.liensAno = liensAno;
    }

    public TypeVersion getTypeVersion()
    {
        return typeVersion;
    }

    public void setTypeVersion(TypeVersion typeVersion)
    {
        this.typeVersion = typeVersion;
    }

    public LocalDate getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation)
    {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateDetection()
    {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection)
    {
        this.dateDetection = dateDetection;
    }

    public LocalDate getDateRelance()
    {
        return dateRelance;
    }

    public void setDateRelance(LocalDate dateRelance)
    {
        this.dateRelance = dateRelance;
    }

    public LocalDate getDateReso()
    {
        return dateReso;
    }

    public void setDateReso(LocalDate dateReso)
    {
        this.dateReso = dateReso;
    }

    public TypeAction getAction()
    {
        return action == null ? TypeAction.VIDE : action;
    }

    public void setAction(TypeAction action)
    {
        this.action = action;
    }

    public boolean isTraitee()
    {
        return traitee;
    }

    public Set<Matiere> getMatieres()
    {
        return matieres == null ? new HashSet<>() : matieres;
    }

    public void setMatieres(Set<Matiere> matieres)
    {
        this.matieres = matieres;
    }

    public GroupeComposant getGroupe()
    {
        return groupe;
    }

    public void setGroupe(GroupeComposant groupe)
    {
        this.groupe = groupe;
    }
}

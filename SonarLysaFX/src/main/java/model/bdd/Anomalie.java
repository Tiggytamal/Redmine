package model.bdd;

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
import javax.persistence.Table;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatAnoSuivi;
import model.enums.Param;
import model.enums.TypeAction;
import model.enums.TypeVersion;
import utilities.Statics;

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
        @NamedQuery(name="Anomalie" + AbstractBDDModele.FINDALL, query="SELECT a FROM Anomalie a LEFT JOIN FETCH a.lotRTC l"),
        @NamedQuery(name="Anomalie" + AbstractBDDModele.FINDINDEX, query="SELECT a FROM Anomalie a WHERE a.lotRTC.lot = :index"),
        @NamedQuery(name="Anomalie" + AbstractBDDModele.RESETTABLE, query="DELETE FROM Anomalie")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_ano_suivi", nullable = false)
    private EtatAnoSuivi etatAnoSuivi;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = true)
    private TypeAction action;

    /*---------- CONSTRUCTEURS ----------*/

    Anomalie()
    {
        typeVersion = TypeVersion.SNAPSHOT;
        etatAnoSuivi = EtatAnoSuivi.NOUVELLE;
        action = TypeAction.VIDE;
        remarque = Statics.EMPTY;
        dateDetection = LocalDate.now();
    }

    Anomalie(LotRTC lotRTC)
    {
        this();
        this.lotRTC = lotRTC;
        creerLiensLotRTC();
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
        if (!getRemarque().isEmpty() || numeroAnoRTC != 0 && etatAnoSuivi == EtatAnoSuivi.NOUVELLE)
            etatAnoSuivi = EtatAnoSuivi.TRAITEE;
        return isTraitee();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création du liens vers l'anomalie dans RTC
     */
    private void creerLiensAnoRTC()
    {
        liensAno = Statics.proprietesXML.getMapParams().get(Param.LIENSANOS) + getLotRTC().getProjetRTC().replace(Statics.SPACE, "%20") + Statics.FINLIENSANO + String.valueOf(numeroAnoRTC);
    }

    /**
     * Création du liens vers le lot dans RTC
     */
    private void creerLiensLotRTC()
    {
        liensLot = Statics.proprietesXML.getMapParams().get(Param.LIENSLOTS) + getLotRTC().getLot();
    }

    /*---------- ACCESSEURS ----------*/

    public LotRTC getLotRTC()
    {
        return lotRTC;
    }

    public void setLotRTC(LotRTC lotRTC)
    {
        this.lotRTC = lotRTC;
        creerLiensLotRTC();
    }

    public int getNumeroAnoRTC()
    {
        return numeroAnoRTC;
    }

    public void setNumeroAnoRTC(int numeroAnoRTC)
    {
        this.numeroAnoRTC = numeroAnoRTC;
        creerLiensAnoRTC();
    }

    public String getEtatRTC()
    {
        return getString(etatRTC);
    }

    public void setEtatRTC(String etatRTC)
    {
        this.etatRTC = etatRTC.trim();
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
        return etatAnoSuivi != EtatAnoSuivi.NOUVELLE;
    }

    public EtatAnoSuivi getEtatAnoSuivi()
    {
        return etatAnoSuivi;
    }

    public void setEtatAnoSuivi(EtatAnoSuivi etatAnoSuivi)
    {
        this.etatAnoSuivi = etatAnoSuivi;
    }
}

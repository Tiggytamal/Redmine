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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import dao.AbstractDao;
import model.enums.EtatDefaut;
import model.enums.Param;
import model.enums.TypeAction;
import model.enums.TypeDefaut;
import model.enums.TypeVersion;
import utilities.Statics;

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

    public EtatDefaut getEtatDefaut()
    {
        return etatDefaut;
    }

    public void setEtatDefaut(EtatDefaut etatAnoSuivi)
    {
        this.etatDefaut = etatAnoSuivi;
    }

    public TypeDefaut getTypeDefaut()
    {
        return typeDefaut;
    }

    public void setTypeDefaut(TypeDefaut typeDefault)
    {
        this.typeDefaut = typeDefault;
    }

    public String getNomCompoAppli()
    {
        return getString(nomCompoAppli);
    }

    public void setNomCompoAppli(String nomCompoAppli)
    {
        this.nomCompoAppli = nomCompoAppli;
    }
    public String getNewCodeAppli()
    {
        return newCodeAppli;
    }

    public void setNewCodeAppli(String newCodeAppli)
    {
        this.newCodeAppli = newCodeAppli;
    }
}

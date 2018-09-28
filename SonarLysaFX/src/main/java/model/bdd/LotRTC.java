package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatLot;
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
                + "LEFT JOIN FETCH l.projetClarity p"),
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
    @ManyToOne (targetEntity = ProjetClarity.class, cascade = CascadeType.MERGE)
    @JoinColumn (name = "projet_Clarity")
    private ProjetClarity projetClarity;
    
    @OneToMany (targetEntity = ComposantSonar.class, fetch = FetchType.LAZY, mappedBy = "lotRTC")
    private List<ComposantSonar> composants;

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

    /*---------- CONSTRUCTEURS ----------*/

    LotRTC() 
    {
        composants = new ArrayList<>();
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
        return this;
        
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
    
    public List<ComposantSonar> getComposants()
    {
        return composants;
    }

    public void setComposants(List<ComposantSonar> composants)
    {
        this.composants = composants;
    }
}

package model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatLot;
import model.utilities.AbstractModele;

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
        @NamedQuery(name="LotSuiviRTC.findAll", query="SELECT l FROM LotSuiviRTC l "
                + "JOIN FETCH l.projetClarity p"),
        @NamedQuery(name="LotSuiviRTC.resetTable", query="DELETE FROM LotSuiviRTC")
})
//@formatter:on
public class LotSuiviRTC extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBase;

    @Column(name = "lot", nullable = false, length = 6)
    private String lot;
    
    @Column(name = "libellé", nullable = false)
    private String libelle;
    
    @BatchFetch(value = BatchFetchType.JOIN)    
    @ManyToOne (targetEntity = InfoClarity.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn (name = "projet_Clarity")
    private InfoClarity projetClarity;

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

    LotSuiviRTC() {}

    /*---------- METHODES PUBLIQUES ----------*/
    
    public LotSuiviRTC update(LotSuiviRTC update)
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

    public int getIdBase()
    {
        return idBase;
    }
    
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

    public InfoClarity getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(InfoClarity projetClarity)
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
}

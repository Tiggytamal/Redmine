package model.bdd;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dao.AbstractMySQLDao;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.QG;
import utilities.Statics;
import utilities.Utilities;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe représentant l'extraction d'un lot depuis RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@Entity(name = "LotRTC")
@Access(AccessType.FIELD)
@Table(name = "lots_RTC")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="LotRTC" + AbstractMySQLDao.FINDALL, query="SELECT l FROM LotRTC l "
                + "JOIN FETCH l.projetClarity pc "
                + "JOIN FETCH l.projetClarity.chefService cs "
                + "JOIN FETCH l.edition e "
                + "JOIN FETCH l.cpiProjet u"),
        @NamedQuery(name="LotRTC" + AbstractMySQLDao.FINDINDEX, query="SELECT l FROM LotRTC l WHERE l.numero = :index"),
        @NamedQuery(name="LotRTC" + AbstractMySQLDao.RESET, query="DELETE FROM LotRTC")
})
//@formatter:on
@XmlRootElement
public class LotRTC extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "numero", nullable = false, length = 6)
    private String numero;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne(targetEntity = ProjetClarity.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "projet_Clarity")
    private ProjetClarity projetClarity;

    // Permet d'enregistrer temporairement la donnée depuis RTC avant traitement
    @Transient
    private String projetClarityString;

    @ManyToOne(targetEntity = Edition.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "edition")
    private Edition edition;

    // Permet d'enregistrer temporairement la donnée depuis RTC avant traitement
    @Transient
    private String editionString;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualityGate", nullable = false)
    private QG qualityGate;

    @ManyToOne(targetEntity = Utilisateur.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "cpi_projet")
    private Utilisateur cpiProjet;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatLot", nullable = true)
    private EtatLot etatLot;

    @Column(name = "projet_RTC", nullable = false, length = 128)
    private String projetRTC;

    @Column(name = "date_maj_etat", nullable = true)
    private LocalDate dateMajEtat;

    @Column(name = "date_mep", nullable = false)
    private LocalDate dateMep;

    @Column(name = "date_repack", nullable = true)
    private LocalDate dateRepack;

    @Column(name = "rtc_hs", nullable = false)
    private boolean rtcHS;

    /*---------- CONSTRUCTEURS ----------*/

    LotRTC()
    {
        qualityGate = QG.NONE;
        rtcHS = false;
        dateMajEtat = LocalDate.of(2016, 1, 1);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getNumero();
    }

    public static LotRTC getLotRTCInconnu(String numero)
    {
        LotRTC retour = new LotRTC();
        retour.setNumero(numero);
        retour.libelle = Statics.EMPTY;
        retour.projetClarityString = Statics.EMPTY;
        retour.editionString = Statics.EMPTY;
        retour.etatLot = EtatLot.NOUVEAU;
        retour.projetRTC = Statics.EMPTY;
        retour.cpiProjet = Statics.USERINCONNU;
        retour.edition = Statics.EDITIONINCONNUE;
        return retour;
    }

    public LotRTC update(LotRTC update)
    {
        setLibelle(update.libelle);
        setProjetClarity(update.projetClarity);
        setCpiProjet(update.cpiProjet);
        setEdition(update.edition);
        setEtatLot(update.etatLot);
        setProjetRTC(update.projetRTC);
        setDateMajEtat(update.dateMajEtat);
        setDateMep(update.dateMep);
        setProjetClarityString(update.projetClarityString);
        setEditionString(update.editionString);
        setRtcHS(update.rtcHS);
        return this;
    }

    /**
     * Calcul le liens vers RTC d'un lot depuis les paramètres de l'application.
     * 
     * @return
     *         Le lien.
     */
    public String getLiens()
    {
        if (getProjetRTC().isEmpty() || getNumero().isEmpty())
            return Statics.EMPTY;

        String liensBrut = Statics.proprietesXML.getMapParams().get(Param.LIENSRTC) + getProjetRTC() + Statics.FINLIENSRTC + String.valueOf(getNumero());
        return getString(Utilities.stringToUrl(liensBrut));
    }
    
    /**
     * Traitement pour mettre à jour la date de Repack du lot.
     * Si la date est bonne et aprés la date initiale, on remplace celle-ci.
     * 
     * @param compo
     *              Composant à traiter.
     */
    public void majDateRepack(LocalDate dateRepack)
    {
        if (Statics.DATEINCO2099.equals(getEdition().getDateMEP()) && (getDateRepack() == null || dateRepack.isAfter(getDateRepack())))
            setDateRepack(dateRepack);
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result
                + Objects.hash(cpiProjet, dateMajEtat, dateMep, dateRepack, edition, editionString, etatLot, libelle, numero, projetClarity, projetClarityString, projetRTC, qualityGate, rtcHS);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        LotRTC other = (LotRTC) obj;
        return Objects.equals(cpiProjet, other.cpiProjet) 
                && Objects.equals(dateMajEtat, other.dateMajEtat) 
                && Objects.equals(dateMep, other.dateMep) 
                && Objects.equals(dateRepack, other.dateRepack)
                && Objects.equals(edition, other.edition) 
                && Objects.equals(editionString, other.editionString) 
                && etatLot == other.etatLot 
                && Objects.equals(libelle, other.libelle)
                && Objects.equals(numero, other.numero) 
                && Objects.equals(projetClarity, other.projetClarity) 
                && Objects.equals(projetClarityString, other.projetClarityString)
                && Objects.equals(projetRTC, other.projetRTC) 
                && qualityGate == other.qualityGate 
                && rtcHS == other.rtcHS;
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNumero()
    {
        return getString(numero);
    }

    public void setNumero(String lot)
    {
        this.numero = getString(lot);
    }

    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = getString(libelle);
    }

    public ProjetClarity getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(ProjetClarity projetClarity)
    {
        if (projetClarity != null)
            this.projetClarity = projetClarity;
    }

    public Utilisateur getCpiProjet()
    {
        return cpiProjet;
    }

    public void setCpiProjet(Utilisateur cpiProjet)
    {
        if (cpiProjet != null)
            this.cpiProjet = cpiProjet;
    }

    public Edition getEdition()
    {
        return edition;
    }

    public void setEdition(Edition edition)
    {
        if (edition != null)
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

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateMep()
    {
        return dateMep;
    }

    public void setDateMep(LocalDate dateMep)
    {
        if (dateMep != null)
            this.dateMep = dateMep;
    }

    public String getProjetRTC()
    {
        return getString(projetRTC);
    }

    public void setProjetRTC(String projetRTC)
    {
        this.projetRTC = getString(projetRTC);
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateMajEtat()
    {
        return dateMajEtat;
    }

    public void setDateMajEtat(LocalDate dateMajEtat)
    {
        if (dateMajEtat != null)
            this.dateMajEtat = dateMajEtat;
    }

    public String getProjetClarityString()
    {
        return getString(projetClarityString);
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
        if (qualityGate != null)
            this.qualityGate = qualityGate;
    }

    public String getEditionString()
    {
        return getString(editionString);
    }

    public void setEditionString(String editionString)
    {
        this.editionString = getString(editionString);
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateRepack()
    {
        return dateRepack;
    }

    public void setDateRepack(LocalDate dateRepack)
    {
        if (dateRepack != null)
            this.dateRepack = dateRepack;
    }

    public boolean isRtcHS()
    {
        return rtcHS;
    }

    public void setRtcHS(boolean rtcHS)
    {
        this.rtcHS = rtcHS;
    }
}

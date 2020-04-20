package model.bdd;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import dao.AbstractMySQLDao;
import model.enums.Param;
import model.fxml.DefautQualiteFXML;
import utilities.Statics;
import utilities.Utilities;

/**
 * Classe de modèle pour les anomalies RTC non créées par l'application et ajoutée à la main.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
@Entity(name = "AnomalieRTC")
@Access(AccessType.FIELD)
@Table(name = "anomalies_rtc")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="AnomalieRTC" + AbstractMySQLDao.FINDALL, query="SELECT a FROM AnomalieRTC a"),
        @NamedQuery(name="AnomalieRTC" + AbstractMySQLDao.FINDINDEX, query="SELECT a FROM AnomalieRTC a WHERE a.numero = :index"),
        @NamedQuery(name="AnomalieRTC" + AbstractMySQLDao.RESET, query="DELETE FROM AnomalieRTC")
})
//@formatter:on
public class AnomalieRTC extends AbstractBDDModele<Integer>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String SUPPORTNICE = "Support NICE";

    @Column(name = "titre", nullable = true)
    private String titre;

    @Column(name = "numero", nullable = false, length = 6)
    private int numero;

    @Column(name = "etat_ano", nullable = false, length = 40)
    private String etatAno;

    @Column(name = "commentaire", nullable = true)
    private String commentaire;
    
    @Column(name = "type_defaut", nullable = true)
    private String typeDefaut;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name = "date_relance", nullable = true)
    private LocalDate dateRelance;

    @Column(name = "date_resolution", nullable = true)
    private LocalDate dateReso;

    @Column(name = "matiere", nullable = true)
    private String matiere;

    @ManyToOne(targetEntity = ProjetClarity.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "projet_Clarity")
    private ProjetClarity clarity;

    @Column(name = "edition", nullable = true)
    private String edition;

    @ManyToOne(targetEntity = Utilisateur.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "cpi_lot")
    private Utilisateur cpiLot;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Integer getMapIndex()
    {
        return getNumero();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(titre, numero, dateCreation, matiere, edition);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        AnomalieRTC other = (AnomalieRTC) obj;
        return Objects.equals(titre, other.titre) 
                && numero == other.numero 
                && Objects.equals(etatAno, other.etatAno) 
                && Objects.equals(commentaire, other.commentaire)
                && Objects.equals(dateCreation, other.dateCreation) 
                && Objects.equals(dateReso, other.dateReso) 
                && Objects.equals(dateRelance, other.dateRelance)
                && Objects.equals(matiere, other.matiere) 
                && Objects.equals(edition, other.edition);
      //@formatter:on
    }

    /**
     * Calcul le liens vers l'anomalie RTC depuis les paramètres de l'application.
     * 
     * @return
     *      Le lien.
     */
    public String getLiens()
    {
        if (numero != 0)
        {
            String liensBrut = Statics.proprietesXML.getMapParams().get(Param.LIENSRTC) + SUPPORTNICE + Statics.FINLIENSRTC + String.valueOf(numero);
            return Utilities.stringToUrl(liensBrut);
        }
        return Statics.EMPTY;
    }

    public DefautQualiteFXML buildDefautQualiteFXML()
    {
        DefautQualiteFXML retour = new DefautQualiteFXML();
        retour.setNumeroAnoRTC(Arrays.asList(String.valueOf(getNumero()), getLiens()));
        retour.setEtatRTC(getEtatAno());
        retour.setRemarque(getCommentaire());
        retour.setTypeDefaut(getTypeDefaut());
        if (getDateCreation() != null)
            retour.setDateCreation(getDateCreation().toString());
        if (getDateRelance() != null)
            retour.setDateRelance(getDateRelance().toString());
        if (getDateReso() != null)
            retour.setDateReso(getDateReso().toString());
        retour.setMatiere(getMatiere());
        retour.setEdition(getEdition());
        if (getCpiLot() != null)
        {
            retour.setEmailCpi(getCpiLot().getEmail());
            retour.setCpiLot(getCpiLot().getNom());
        }
        if (getClarity() != null)
        {
            retour.setDepartement(getClarity().getDepartement());
            retour.setService(getClarity().getService());
            retour.setDirection(getClarity().getDirection());
            retour.setLibelleClarity(getClarity().getLibelleProjet());
            if (getClarity().getChefService() != null)
                retour.setRespServ(getClarity().getChefService().getNom());
            retour.setCodeClarity(getClarity().getCode());
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getTitre()
    {
        return getString(titre);
    }

    public void setTitre(String titre)
    {
        this.titre = getString(titre);
    }

    public int getNumero()
    {
        return numero;
    }

    public void setNumero(int numero)
    {
        if (numero != 0)
            this.numero = numero;
    }

    public String getCommentaire()
    {
        return getString(commentaire);
    }

    public void setCommentaire(String commentaire)
    {
        this.commentaire = getString(commentaire);
    }

    public LocalDate getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation)
    {
        if (dateCreation != null)
            this.dateCreation = dateCreation;
    }

    public LocalDate getDateReso()
    {
        return dateReso;
    }

    public void setDateReso(LocalDate dateReso)
    {
        if (dateReso != null)
            this.dateReso = dateReso;
    }

    public LocalDate getDateRelance()
    {
        return dateRelance;
    }

    public void setDateRelance(LocalDate dateRelance)
    {
        if (dateRelance != null)
            this.dateRelance = dateRelance;
    }

    public String getEtatAno()
    {
        return getString(etatAno);
    }

    public void setEtatAno(String etatAno)
    {
        this.etatAno = getString(etatAno);
    }

    public String getMatiere()
    {
        return getString(matiere);
    }

    public void setMatiere(String matiere)
    {
        this.matiere = getString(matiere);
    }

    public ProjetClarity getClarity()
    {
        return clarity;
    }

    public void setClarity(ProjetClarity clarity)
    {
        if (clarity != null)
            this.clarity = clarity;
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = getString(edition);
    }

    public Utilisateur getCpiLot()
    {
        return cpiLot;
    }

    public void setCpiLot(Utilisateur cpiLot)
    {
        if (cpiLot != null)
            this.cpiLot = cpiLot;
    }

    public String getTypeDefaut()
    {
        return getString(typeDefaut);
    }

    public void setTypeDefaut(String typeDefaut)
    {
        this.typeDefaut = getString(typeDefaut);
    }
}

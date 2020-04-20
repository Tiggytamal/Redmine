package model.bdd;

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
import utilities.Statics;

/**
 * Classe représentant un projet Clarity extrait du fichier hebdomadaire des projets.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity(name = "ProjetClarity")
@Access(AccessType.FIELD)
@Table(name = "projets_clarity")
//@formatter:off
@NamedQueries (value = {
      @NamedQuery(name="ProjetClarity" + AbstractMySQLDao.FINDALL, query="SELECT pc FROM ProjetClarity pc "
              + "JOIN FETCH pc.chefService cs"),
      @NamedQuery(name="ProjetClarity" + AbstractMySQLDao.FINDINDEX, query="SELECT pc FROM ProjetClarity pc WHERE pc.code = :index"),
      @NamedQuery(name="ProjetClarity" + AbstractMySQLDao.RESET, query="DELETE FROM ProjetClarity")

})
//@formatter:on
public class ProjetClarity extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    public static final String INCONNU = "Code Clarity inconnu du référentiel";

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "libelle_projet", nullable = false)
    private String libelleProjet;

    @Column(name = "chef_de_projet", nullable = false)
    private String chefProjet;

    @Column(name = "edition", nullable = false)
    private String edition;

    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "departement", nullable = false)
    private String departement;

    @Column(name = "service", nullable = false)
    private String service;

    @ManyToOne(targetEntity = ChefService.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "chef_service")
    private ChefService chefService;

    /*---------- CONSTRUCTEURS ----------*/

    ProjetClarity()
    {}

    public static ProjetClarity getProjetClarityInconnu(String code)
    {
        ProjetClarity retour = new ProjetClarity();
        retour.code = code;
        retour.actif = false;
        retour.libelleProjet = INCONNU;
        retour.chefProjet = Statics.INCONNU;
        retour.edition = Statics.INCONNU;
        retour.direction = Statics.INCONNUE;
        retour.departement = Statics.INCONNU;
        retour.service = Statics.INCONNU;
        retour.chefService = Statics.CHEFSERVINCONNU;
        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getCode();
    }

    public ProjetClarity update(ProjetClarity update)
    {
        actif = update.actif;
        libelleProjet = update.libelleProjet;
        chefProjet = update.chefProjet;
        edition = update.edition;
        direction = update.direction;
        departement = update.departement;
        service = update.service;
        return this;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(actif, chefProjet, chefService, code, departement, direction, edition, libelleProjet, service);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        ProjetClarity other = (ProjetClarity) obj;
        return actif == other.actif 
                && Objects.equals(chefProjet, other.chefProjet) 
                && Objects.equals(chefService, other.chefService) 
                && Objects.equals(code, other.code)
                && Objects.equals(departement, other.departement) 
                && Objects.equals(direction, other.direction) 
                && Objects.equals(edition, other.edition)
                && Objects.equals(libelleProjet, other.libelleProjet) 
                && Objects.equals(service, other.service);
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = getString(code);
    }

    public String getLibelleProjet()
    {
        return getString(libelleProjet);
    }

    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = getString(libelleProjet);
    }

    public String getChefProjet()
    {
        return getString(chefProjet);
    }

    public void setChefProjet(String chefProjet)
    {
        this.chefProjet = getString(chefProjet);
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = getString(edition);
    }

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = getString(direction);
    }

    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = getString(departement);
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = getString(service);
    }

    public ChefService getChefService()
    {
        return chefService;
    }

    public void setChefService(ChefService chefService)
    {
        this.chefService = chefService;
    }
}

package model.bdd;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import utilities.Statics;

/**
 * Classe représentant un projet Clarity extrait du fichier hebdomadaire des projets.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity
@Table(name = "projets_clarity")
//@formatter:off
@NamedQueries (value = {
      @NamedQuery(name="ProjetClarity.findAll", query="SELECT distinct(pc) FROM ProjetClarity pc "
              + "JOIN FETCH pc.chefService cs"),
      @NamedQuery(name="ProjetClarity.resetTable", query="DELETE FROM ProjetClarity"),
      @NamedQuery(name="ProjetClarity.findByCode", query="SELECT pc FROM ProjetClarity pc WHERE pc.code = :code")
})
//@formatter:on
public class ProjetClarity extends AbstractBDDModele
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String INCONNU = "Code Clarity inconnu du réferentiel";

    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "actif", nullable = false)
    private boolean actif;
    
    @Column(name = "libellé_projet", nullable = false)
    private String libelleProjet;
    
    @Column(name = "chef_de_projet", nullable = false)
    private String chefProjet;
    
    @Column(name = "édition", nullable = false)
    private String edition;
    
    @Column(name = "direction", nullable = false)
    private String direction;
    
    @Column(name = "département", nullable = false)
    private String departement;
    
    @Column(name = "service", nullable = false)
    private String service;
    
    @BatchFetch(value = BatchFetchType.JOIN)    
    @ManyToOne (targetEntity = ChefService.class, cascade = CascadeType.MERGE)
    @JoinColumn (name = "chef_service")
    private ChefService chefService;
    
    

    /*---------- CONSTRUCTEURS ----------*/

    ProjetClarity() { }
    
    public static ProjetClarity getProjetClarityInconnu(String code)
    {
        ProjetClarity retour = new ProjetClarity();
        retour.code = code;
        retour.actif = false;
        retour.libelleProjet = INCONNU;
        retour.chefProjet = Statics.INCONNU;
        retour.edition = Statics.INCONNU;
        retour.direction = Statics.INCONNU;
        retour.departement = Statics.INCONNU;
        retour.service = Statics.INCONNU;
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
        this.code = code;
    }

    public String getLibelleProjet()
    {
        return getString(libelleProjet);
    }

    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = libelleProjet;
    }

    public String getChefProjet()
    {
        return getString(chefProjet);
    }

    public void setChefProjet(String chefProjet)
    {
        this.chefProjet = chefProjet;
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = service;
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

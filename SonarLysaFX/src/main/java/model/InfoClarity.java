package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import model.utilities.AbstractModele;
import utilities.Statics;

/**
 * Classe représentant un projet Clarity extrait du fichier hebdomadaire des projets.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity
@Table(name = "infos_clarity")
//@formatter:off
@NamedQueries (value = {
      @NamedQuery(name="InfoClarity.findAll", query="SELECT distinct(ic) FROM InfoClarity ic "
              + "JOIN FETCH ic.chefService cs"),
      @NamedQuery(name="InfoClarity.resetTable", query="DELETE FROM InfoClarity"),
      @NamedQuery(name="InfoClarity.findByCode", query="SELECT ic FROM InfoClarity ic WHERE ic.codeClarity = :code")
})
//@formatter:on
public class InfoClarity extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBase;

    @Column(name = "code_Clarity", nullable = false)
    private String codeClarity;
    
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
    @ManyToOne (targetEntity = ChefService.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn (name = "chef_service")
    private ChefService chefService;
    
    

    /*---------- CONSTRUCTEURS ----------*/

    InfoClarity() { }
    
    /**
     * Costructeur pour projet Clarity inconnu
     * 
     * @param codeClarity
     */
    InfoClarity(String codeClarity)
    {
        this.codeClarity = codeClarity;
        actif = false;
        libelleProjet = "Code Clarity inconnu du réferentiel";
        chefProjet = Statics.EMPTY;
        edition = Statics.EMPTY;
        direction = Statics.EMPTY;
        departement = Statics.EMPTY;
        service = Statics.EMPTY;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public InfoClarity update(InfoClarity update)
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

    public int getIdBase()
    {
        return idBase;
    }

    public void setIdBase(int idBase)
    {
        this.idBase = idBase;
    }
    
    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

    public String getCodeClarity()
    {
        return getString(codeClarity);
    }

    public void setCodeClarity(String codeClarity)
    {
        this.codeClarity = codeClarity;
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

    /**
     * @return the direction
     */
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

package model.bdd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import utilities.Statics;

/**
 * Classe de modèle du fichier Excel des chefs de services
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@Entity
@Table(name = "chefs_de_service")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ChefService.findAll", query="SELECT c FROM ChefService c"),
        @NamedQuery(name="ChefService.findByIndex", query="SELECT c FROM ChefService c WHERE c.service = :index"),
        @NamedQuery(name="ChefService.resetTable", query="DELETE FROM ChefService")
})
//@formatter:on
public class ChefService extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String INCONNU = "Chef de Service inconnu";

    @Column(name = "filière", nullable = false)
    private String filiere;

    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "service", nullable = false)
    private String service;

    @Column(name = "departement", nullable = false)
    private String departement;

    @Column(name = "nom", nullable = false)
    private String nom;

    /*---------- CONSTRUCTEURS ----------*/

    ChefService() { }
    
    public static ChefService getChefServiceInconnu(String service)
    {
        ChefService retour = new ChefService();
        retour.nom = INCONNU;
        retour.filiere = Statics.EMPTY;
        retour.direction = Statics.EMPTY;
        retour.departement = Statics.EMPTY;
        retour.service = service;
        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getService();
    }
    
    public ChefService update(ChefService chef)
    {
        filiere = chef.filiere;
        direction = chef.direction;
        service = chef.service;
        departement = chef.departement;
        return this;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getFiliere()
    {
        return getString(filiere);
    }

    public void setFiliere(String filiere)
    {
        this.filiere = filiere;
    }

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
}

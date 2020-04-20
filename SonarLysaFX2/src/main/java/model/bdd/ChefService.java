package model.bdd;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import dao.AbstractMySQLDao;
import utilities.Statics;

/**
 * Classe de modele du fichier Excel des chefs de services.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@Entity(name = "ChefService")
@Access(AccessType.FIELD)
@Table(name = "chefs_de_service")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ChefService"  + AbstractMySQLDao.FINDALL, query="SELECT c FROM ChefService c"),
        @NamedQuery(name="ChefService" + AbstractMySQLDao.FINDINDEX, query="SELECT c FROM ChefService c WHERE c.service = :index"),
        @NamedQuery(name="ChefService" + AbstractMySQLDao.RESET, query="DELETE FROM ChefService")
})
//@formatter:on
public class ChefService extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String INCONNU = "Chef de Service inconnu";

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "filière", nullable = false)
    private String filiere;

    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "service", nullable = false)
    private String service;

    @Column(name = "departement", nullable = false)
    private String departement;

    /*---------- CONSTRUCTEURS ----------*/

    ChefService()
    {}

    public static ChefService getChefServiceInconnu(String service)
    {

        ChefService retour = new ChefService();
        retour.nom = INCONNU;
        retour.filiere = Statics.EMPTY;
        retour.direction = Statics.EMPTY;
        retour.departement = Statics.EMPTY;
        retour.setService(service);
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

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(departement, direction, filiere, nom, service);
        return result; 
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        ChefService other = (ChefService) obj;
        return Objects.equals(departement, other.departement) 
                && Objects.equals(direction, other.direction) 
                && Objects.equals(filiere, other.filiere) 
                && Objects.equals(nom, other.nom)
                && Objects.equals(service, other.service);
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getFiliere()
    {
        return getString(filiere);
    }

    public void setFiliere(String filiere)
    {
        this.filiere = getString(filiere);
    }

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = getString(direction);
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = getString(service);
    }

    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = getString(departement);
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }
}

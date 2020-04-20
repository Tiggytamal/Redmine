package model.bdd;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.ibm.team.repository.common.IContributor;

import dao.AbstractMySQLDao;
import dao.Dao;
import dao.DaoFactory;

/**
 * Classe de modele représentant les utilisateurs RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * 
 */
@Entity
@Table(name = "utilisateurs")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Utilisateur" + AbstractMySQLDao.FINDALL, query="SELECT u FROM Utilisateur u"),
        @NamedQuery(name="Utilisateur" + AbstractMySQLDao.FINDINDEX, query="SELECT u FROM Utilisateur u WHERE u.identifiant = :index"),
        @NamedQuery(name="Utilisateur" + AbstractMySQLDao.RESET, query="DELETE FROM Utilisateur")
})
//@formatter:on
@XmlRootElement
public class Utilisateur extends AbstractBDDModele<String>
{

    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "nom", nullable = false, length = 64)
    private String nom;

    @Column(name = "identifiant", nullable = false, length = 7)
    private String identifiant;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "active")
    private boolean active;

    /*---------- CONSTRUCTEURS ----------*/

    Utilisateur()
    {
        // Constructeur vide pour JPA
    }

    private Utilisateur(String nom, String identifiant, String email)
    {
        this.nom = nom;
        this.identifiant = identifiant;
        this.email = email;
        active = true;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getIdentifiant();
    }

    /**
     * Création d'un utilisateur depuis les données de RTC, et persistance en base si celui-ci n'existe pas encore.
     * 
     * @param owner
     *              L'objet à persister depuis RTC.
     * @return
     *         Un Utilisateur correspondant à l'objet provenant de RTC.
     */
    public static Utilisateur build(IContributor owner)
    {
        Dao<Utilisateur, String> dao = DaoFactory.getMySQLDao(Utilisateur.class);
        Utilisateur userBase = dao.recupEltParIndex(owner.getUserId().trim());
        if (userBase == null)
        {
            userBase = new Utilisateur(owner.getName(), owner.getUserId(), owner.getEmailAddress());
            dao.persist(userBase);
        }
        return userBase;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(idBase, email, identifiant, nom, active);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        Utilisateur other = (Utilisateur) obj;
        return idBase == other.idBase
                && Objects.equals(email, other.email) 
                && Objects.equals(identifiant, other.identifiant) 
                && Objects.equals(nom, other.nom)
                && active == other.active;
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    public String getIdentifiant()
    {
        return getString(identifiant);
    }

    public void setIdentifiant(String identifiant)
    {
        this.identifiant = getString(identifiant);
    }

    public String getEmail()
    {
        return getString(email);
    }

    public void setEmail(String email)
    {
        this.email = getString(email);
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}

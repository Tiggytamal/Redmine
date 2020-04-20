package model.bdd;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import dao.AbstractMySQLDao;

/**
 * Classe de modele représentant le lien entre un projet de la pic Mobile Center et un numéro de lot.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@Entity
@Table(name = "projets_mobile")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ProjetMobileCenter" + AbstractMySQLDao.FINDALL, query="SELECT p FROM ProjetMobileCenter p"),
        @NamedQuery(name="ProjetMobileCenter" + AbstractMySQLDao.FINDINDEX, query="SELECT p FROM ProjetMobileCenter p WHERE p.nom = :index"),
        @NamedQuery(name="ProjetMobileCenter" + AbstractMySQLDao.RESET, query="DELETE FROM ProjetMobileCenter")
})
//@formatter:on
@XmlRootElement
public class ProjetMobileCenter extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @Column(name = "numero_lot", nullable = false)
    private int numeroLot;

    /*---------- CONSTRUCTEURS ----------*/

    ProjetMobileCenter()
    {}

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getNom();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(nom, numeroLot);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        ProjetMobileCenter other = (ProjetMobileCenter) obj;
        return Objects.equals(nom, other.nom) 
                && numeroLot == other.numeroLot;
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

    public int getNumeroLot()
    {
        return numeroLot;
    }

    public void setNumeroLot(int numeroLot)
    {
        this.numeroLot = numeroLot;
    }
}

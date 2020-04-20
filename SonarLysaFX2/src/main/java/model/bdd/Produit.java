package model.bdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dao.AbstractMySQLDao;
import model.enums.Param;
import utilities.Statics;

/**
 * Classe de modele qui correspond aux produit dans l'application appCATS.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity(name = "Produit")
@Access(AccessType.FIELD)
@Table(name = "produits")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Produit" + AbstractMySQLDao.FINDALL, query="SELECT p FROM Produit p"),
        @NamedQuery(name="Produit" + AbstractMySQLDao.FINDINDEX, query="SELECT p FROM Produit p WHERE p.nom = :index"),
        @NamedQuery(name="Produit" + AbstractMySQLDao.RESET, query="DELETE FROM Produit")
})
//@formatter:on
public class Produit extends AbstractBDDModele<String>
{

    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @OneToMany(targetEntity = Solution.class, cascade = CascadeType.MERGE, mappedBy = "produit")
    private List<Solution> listeSolution;

    /*---------- CONSTRUCTEURS ----------*/

    Produit()
    {
        listeSolution = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getNom();
    }

    public String calculLiens()
    {
        return Statics.proprietesXML.getMapParams().get(Param.URLMAPSPRODUIT) + getNom();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(nom);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        Produit other = (Produit) obj;
        return Objects.equals(nom, other.nom);
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
        if (nom != null && !nom.isEmpty())
            this.nom = nom;
    }

    public List<Solution> getListeSolution()
    {
        if (listeSolution == null)
            listeSolution = new ArrayList<>();
        return listeSolution;
    }

    public void setListeSolution(List<Solution> listeSolution)
    {
        if (listeSolution != null && !listeSolution.isEmpty())
            this.listeSolution = listeSolution;
    }
}

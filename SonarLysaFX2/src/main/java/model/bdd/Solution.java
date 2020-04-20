package model.bdd;

import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dao.AbstractMySQLDao;

/**
 * Classe de modele qui correspond Solutions de l'appplication AppCATS.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity(name = "Solution")
@Access(AccessType.FIELD)
@Table(name = "solutions")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Solution" + AbstractMySQLDao.FINDALL, query="SELECT p FROM Produit p"),
        @NamedQuery(name="Solution" + AbstractMySQLDao.FINDINDEX, query="SELECT p FROM Produit p WHERE p.nom = :index"),
        @NamedQuery(name="Solution" + AbstractMySQLDao.RESET, query="DELETE FROM Produit")
})
//@formatter:on
public class Solution extends AbstractBDDModele<String>
{

    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @ManyToOne(targetEntity = Produit.class, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "produit")
    private Produit produit;

    @OneToMany(targetEntity = ComposantBase.class, cascade = CascadeType.MERGE, mappedBy = "solution")
    private List<ComposantBase> listeComposants;

    /*---------- CONSTRUCTEURS ----------*/

    Solution()
    {
        listeComposants = new ArrayList<>();
    }

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
        result = PRIME * result + Objects.hash(nom, produit);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj)) 
            return false;
        Solution other = (Solution) obj;
        return Objects.equals(nom, other.nom) 
                && Objects.equals(produit, other.produit);
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

    public Produit getProduit()
    {
        return produit;
    }

    public void setProduit(Produit produit)
    {
        if (produit != null)
        {
            this.produit = produit;

            // Ajout bidirectionnel du produit
            if (!produit.getListeSolution().contains(this))
                produit.getListeSolution().add(this);
        }
    }

    public List<ComposantBase> getListeComposants()
    {
        if (listeComposants == null)
            listeComposants = new ArrayList<>();
        return listeComposants;
    }

    public void setListeComposants(List<ComposantBase> listeComposants)
    {
        if (listeComposants != null && !listeComposants.isEmpty())
            this.listeComposants = listeComposants;
    }
}

package model.bdd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import dao.AbstractDao;
import model.enums.GroupeProduit;

/**
 * Classe de modèle qui correspond aux données du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "produits")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Produit" + AbstractDao.FINDALL, query="SELECT p FROM Produit p"),
        @NamedQuery(name="Produit" + AbstractDao.FINDINDEX, query="SELECT p FROM Produit p WHERE p.nomProjet = :index"),
        @NamedQuery(name="Produit" + AbstractDao.RESET, query="DELETE FROM Produit")
})
//@formatter:on
public class Produit extends AbstractBDDModele
{

    /*---------- ATTRIBUTS ----------*/

    @Column(name = "projet", nullable = false, length = 128)
    private String nomProjet;

    @Enumerated(EnumType.STRING)
    @Column(name = "groupe")
    private GroupeProduit groupe;

    /*---------- CONSTRUCTEURS ----------*/

    Produit() { }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getNomProjet();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNomProjet()
    {
        return getString(nomProjet);
    }

    public void setNomProjet(String nomProjet)
    {
        if (nomProjet != null && !nomProjet.isEmpty())
            this.nomProjet = nomProjet;
    }

    public GroupeProduit getGroupe()
    {
        if (groupe == null)
            return GroupeProduit.AUCUN;
        return groupe;
    }

    public void setGroupe(GroupeProduit groupe)
    {
        if (groupe != null)
            this.groupe = groupe;
    }
}

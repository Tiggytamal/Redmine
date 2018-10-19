package model.bdd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import dao.AbstractDao;
import model.enums.GroupeProjet;

/**
 * Classe de modèle qui correspond aux données du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "projets_groupe")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="GroupementProjet" + AbstractDao.FINDALL, query="SELECT gp FROM GroupementProjet gp"),
        @NamedQuery(name="GroupementProjet" + AbstractDao.FINDINDEX, query="SELECT gp FROM GroupementProjet gp WHERE gp.nomProjet = :index"),
        @NamedQuery(name="GroupementProjet" + AbstractDao.RESET, query="DELETE FROM GroupementProjet")
})
//@formatter:on
public class GroupementProjet extends AbstractBDDModele
{

    /*---------- ATTRIBUTS ----------*/

    @Column(name = "projet", nullable = false, length = 128)
    private String nomProjet;

    @Enumerated(EnumType.STRING)
    @Column(name = "groupe")
    private GroupeProjet groupe;

    /*---------- CONSTRUCTEURS ----------*/

    GroupementProjet()
    {
    }

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
        this.nomProjet = nomProjet;
    }

    public GroupeProjet getGroupe()
    {
        if (groupe == null)
            return GroupeProjet.AUCUN;
        return groupe;
    }

    public void setGroupe(GroupeProjet groupe)
    {
        this.groupe = groupe;
    }
}

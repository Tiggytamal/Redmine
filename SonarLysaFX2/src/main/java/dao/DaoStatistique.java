package dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.logging.log4j.Logger;

import model.bdd.Statistique;
import model.enums.StatistiqueEnum;
import utilities.Utilities;

/**
 * DOA pour la classe {@link model.bdd.Statistique}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class DaoStatistique extends AbstractMySQLDao<Statistique, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "fichiers_par_jour";
    public static final String READALLPARTYPE = ".readAllParType";
    private static final Logger LOGCONSOLE = Utilities.getLogger("console-log");

    /*---------- CONSTRUCTEURS ----------*/

    DaoStatistique()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(Statistique t)
    {
        // Pas d'implementation nécessaire
    }

    /**
     * Récupération d'un élément de la base selon la date et le type de statistique recherchée.
     * 
     * @param index
     *              Date de la donnée.
     * @param type
     *              Type de statistique.
     * @return
     *         La statistique trouvée ou null.
     */
    public Statistique recupEltParIndexEtParType(String index, StatistiqueEnum type)
    {
        List<Statistique> liste = em.createNamedQuery(modele.getSimpleName() + FINDINDEX, modele).setParameter(INDEX, LocalDate.parse(index)).getResultList();
        if (!liste.isEmpty())
        {
            for (Statistique stat : liste)
            {
                if (stat.getType() == type)
                    return stat;
            }
        }
        return null;
    }

    /**
     * Retourne toutes les statistiques d'un type donnée.
     * 
     * @param type
     *             Type de la statistique à retourner.
     * @return
     *         Liste des éléments ne base de données.
     */
    public List<Statistique> recupAllParType(StatistiqueEnum type)
    {
        // Appel de la requete et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        TypedQuery<Statistique> query = em.createNamedQuery(modele.getSimpleName() + READALLPARTYPE, modele).setParameter("type", type);
        List<Statistique> retour = query.getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requete ReadAllParType " + modele + " = " + (fin - debut) + " ms");

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

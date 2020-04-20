package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.logging.log4j.Logger;

import model.bdd.ComposantBase;
import model.bdd.StatistiqueCompo;
import model.bdd.StatistiqueCompoIndex;
import model.enums.StatistiqueCompoEnum;
import utilities.Utilities;

/**
 * DOA pour la classe {@link model.bdd.StatistiqueCompo}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class DaoStatistiqueCompo extends AbstractMySQLDao<StatistiqueCompo, StatistiqueCompoIndex>
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String READALLPARTYPE = ".readAllParType";
    public static final String RECUPCOMPOSKEY = ".readAllCompoKey";
    
    private static final long serialVersionUID = 1L;
    private static final String TABLE = "statistiques_par_compo";    
    private static final Logger LOGCONSOLE = Utilities.getLogger("console-log");

    /*---------- CONSTRUCTEURS ----------*/

    DaoStatistiqueCompo()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(StatistiqueCompo t)
    {
        // Persistance ComposantBase
        persistSousObjet(ComposantBase.class, t.getCompo());
    }

    @Override
    public StatistiqueCompo recupEltParIndex(StatistiqueCompoIndex index)
    {
        // Appel de la requete et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        List<StatistiqueCompo> liste = em.createNamedQuery(modele.getSimpleName() + FINDINDEX, modele).setParameter("date", index.getDate()).setParameter("nom", index.getCompo().getNom())
                .setParameter("type", index.getType()).getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requête ReadAllParType " + modele + " = " + (fin - debut) + " ms");

        // Retour résultat
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    /**
     * Retourne toutes les statistiques d'un type donnée.
     * 
     * @param type
     *             Type de la statistique à retourner.
     * @return
     *         Liste des éléments ne base de données.
     */
    public List<StatistiqueCompo> recupAllParType(StatistiqueCompoEnum type)
    {
        // Appel de la requete et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        TypedQuery<StatistiqueCompo> query = em.createNamedQuery(modele.getSimpleName() + READALLPARTYPE, modele).setParameter("type", type);
        List<StatistiqueCompo> retour = query.getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requête recupAllParType " + modele + " = " + (fin - debut) + " ms");
        return retour;
    }
    
    public List<String> recupTousCompos()
    {
        // Appel de la requete et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        TypedQuery<String> query = em.createNamedQuery(modele.getSimpleName() + RECUPCOMPOSKEY, String.class);
        List<String> retour = query.getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requête recupTousCompos " + modele + " = " + (fin - debut) + " ms");
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

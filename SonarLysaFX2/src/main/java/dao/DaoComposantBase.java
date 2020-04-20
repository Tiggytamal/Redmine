package dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

import model.KeyDateMEP;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.bdd.Solution;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.Utilities;

/**
 * DAO pour la classe {@link model.bdd.ComposantBase}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoComposantBase extends AbstractMySQLDao<ComposantBase, String>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger de debug console */
    private static final Logger LOGCONSOLE = Utilities.getLogger("console-log");

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "composants";

    /*---------- CONSTRUCTEURS ----------*/

    DaoComposantBase()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void persistImpl(ComposantBase compo)
    {
        // Presistance application liee
        persistSousObjet(Application.class, compo.getAppli());

        // Persistance lot lie
        persistSousObjet(LotRTC.class, compo.getLotRTC());

        // Persistance solution lie
        persistSousObjet(Solution.class, compo.getSolution());
    }

    /**
     * Récupération d'un composant vaec son nom.
     * 
     * @param nom
     *            Nom du composant.
     * @return
     *         Le composant ou null.
     */
    public ComposantBase recupCompoByNom(String nom)
    {
        long debut = System.currentTimeMillis();
        List<ComposantBase> retour = em.createNamedQuery(modele.getSimpleName() + ".recupCompoByNom", ComposantBase.class).setParameter("index", nom).getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requete DaoComposantBase.recupCompoByNom " + modele + " = " + (fin - debut) + " ms");

        // Retour de la donnée
        if (retour.isEmpty())
            return null;
        else if (retour.size() > 1)
        {
            StringBuilder builder = new StringBuilder();
            for (ComposantBase compo : retour)
            {
                builder.append(compo.getNom() + Statics.NL);
            }
            throw new FunctionalException(Severity.INFO, "Plusieurs composants ont été trouvés : \n" + builder.toString());
        }
        else
            return retour.get(0);
    }

    /**
     * Récupération de la liste des numéros de lots avec un composant Sonar.
     * 
     * @return
     *         La liste des numéros de lot.
     */
    public List<String> recupLotsAvecComposants()
    {
        long debut = System.currentTimeMillis();
        List<String> retour = em.createNamedQuery(modele.getSimpleName() + ".recupLotsRTC", String.class).getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requete DaoComposantBase.recupLotsAvecComposants " + modele + " = " + (fin - debut) + " ms");

        return retour;
    }

    /**
     * Récupération des composants mis en production entre les dates données
     * 
     * @param debut
     *              Date de début de la période.
     * @param fin
     *              Date de fin de la période.
     * @return
     *         La liste de clefs des composants.
     */
    public List<KeyDateMEP> recupKeyComposTEP(LocalDate debut, LocalDate fin)
    {
        long debutTime = System.currentTimeMillis();
        List<KeyDateMEP> retour = em.createNamedQuery(modele.getSimpleName() + ".keyComposTEP", KeyDateMEP.class).setParameter("dateDebut", debut).setParameter("dateFin", fin).getResultList();
        long finTime = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requete DaoComposantBase.recupKeyComposTEP " + modele + " = " + (finTime - debutTime) + " ms");

        return retour;
    }

    /**
     * Retourne le nombre de lignes d'un composant.
     * 
     * @return
     *         Le nombre de ligne du composant ou 0.
     */
    public long recupNombreLigne()
    {
        try
        {
            long debut = System.currentTimeMillis();
            long retour = em.createNamedQuery(modele.getSimpleName() + ".lignesTotal", Long.class).setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache).getSingleResult();
            long fin = System.currentTimeMillis();

            // Affichage des temps de traitement
            LOGCONSOLE.info("temps requete DaoComposantBase.recupNombreLigne " + modele + " = " + (fin - debut) + " ms");

            return retour;
        }
        catch (NullPointerException e)
        {
            return 0L;
        }
    }

    /**
     * Retourne le nombre de composants propres c'est-à-dire de composants sans critiques ou bloquants.
     * 
     * @return
     */
    public int composPropes()
    {
        return em.createNamedQuery(modele.getSimpleName() + ".composPropres", Long.class).getSingleResult().intValue();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

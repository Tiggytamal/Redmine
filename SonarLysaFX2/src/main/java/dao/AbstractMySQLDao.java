package dao;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.Logger;

import model.bdd.AbstractBDDModele;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe abstraite de création de l'Entity Manager. Toutes les classes de DAO vont hériter de cette classe.
 * Implémente l'interface Dao. Appele la base de données MySQL.
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 * @param <T>
 *        Correspond à la classe de modele que l'on va gérer.
 */
public abstract class AbstractMySQLDao<T extends AbstractBDDModele<I>, I> implements Serializable, Dao<T, I>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    private static final Properties jpaProperties = Utilities.recupFichierProperties("jpa.properties");
    private static final Properties mysqlProperties = Utilities.recupFichierProperties("mysql.properties");
    private static final Logger LOGCONSOLE = Utilities.getLogger("console-log");

    /** Lancement au besoin de la base de données, avec un initialiseur statique. Il faut créer la connexion avant les EntityManagers */
    static
    {
        if (new File((String) mysqlProperties.get("mysqladminpath")).exists())
        {
            // Demarrage du serveur MySQl si besoin
            ProcessBuilder pb = new ProcessBuilder((String) mysqlProperties.get("mysqladminpath"), "-u", "root", "-pAQPadmin01", "ping");
            try
            {
                Process p = pb.start();

                if (p.waitFor() == 1)
                    new ProcessBuilder((String) mysqlProperties.get("mysqldpath"), "--console", "--default-time-zone=Europe/Paris").start();
            }
            catch (IOException | InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new TechnicalException("Probleme lors du demarrage du serveur MySQL.", e);
            }
        }
    }

    // Entitymanager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SonarLysaFX", jpaProperties);
    protected static final EntityManager em = emf.createEntityManager();

    // Constantes de requêtes
    public static final String FINDALL = ".findAll";
    public static final String FINDINDEX = ".findByIndex";
    public static final String RESET = ".resetTable";
    protected static final String INDEX = "index";
    protected static final String ALTERTABLE = "ALTER TABLE ";
    protected static final String AUTOINC = " AUTO_INCREMENT = 0";

    protected Class<T> modele;
    protected String nomTable;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractMySQLDao(String nomTable)
    {
        this.nomTable = nomTable;
        buildDao();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Methode à implementer si besoin de fonctions supplementaires lors de la persistance
     * 
     * @param t
     *          Objet à persister.
     */
    protected abstract void persistImpl(T t);

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Pas de données depuis Excel
        return 0;
    }

    @Override
    public void clearCache()
    {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public List<T> readAll()
    {
        // Appel de la requete et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        TypedQuery<T> query = em.createNamedQuery(modele.getSimpleName() + FINDALL, modele);
        List<T> retour = query.getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requete ReadAll " + modele + " = " + (fin - debut) + " ms");

        return retour;
    }

    @Override
    public final Map<I, T> readAllMap()
    {
        // Récupération des objets de la base et initialisation de la hashmap de retour
        List<T> liste = readAll();
        Map<I, T> retour = new HashMap<>((int) (liste.size() * Statics.RATIOLOAD));

        for (T t : liste)
        {
            retour.put(t.getMapIndex(), t);
        }
        return retour;
    }

    @Override
    public T recupEltParIndex(I index)
    {
        List<T> liste = em.createNamedQuery(modele.getSimpleName() + FINDINDEX, modele).setParameter(INDEX, index).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    @Override
    public int persist(Iterable<T> collection)
    {
        if (collection == null)
            return 0;

        em.getTransaction().begin();
        int i = 0;
        for (T t : collection)
        {
            if (persist(t))
                i++;
        }
        em.getTransaction().commit();
        return i;
    }

    @Override
    public boolean persist(T t)
    {
        if (t == null)
            return false;

        // Utilisation du try catch pour récupérer une technical exception et les données posant problème
        try
        {
            // Booleen permettant de créer ou non une transaction SQL
            boolean test = em.getTransaction().isActive();

            if (!test)
                em.getTransaction().begin();

            if (t.getIdBase() == 0)
            {
                // Si l'objet est nouveau, on crée un premier TimeStamp et on appelle l'implementation specifique de l'objet
                t.updateTimeStamp();
                persistImpl(t);
                em.persist(t);
            }
            else
            {
                // Sinon, on fait un simple merge dans le contexte
                persistImpl(t);
                em.merge(t);
            }

            if (!test)
                em.getTransaction().commit();
        }
        catch (Exception e)
        {
            throw new TechnicalException("Plantage enregistrement données : " + t.toString(), e);
        }
        return true;
    }

    @Override
    public int delete(Iterable<T> collection)
    {
        em.getTransaction().begin();
        int i = 0;
        for (T t : collection)
        {
            delete(t);
            i++;
        }
        em.getTransaction().commit();
        return i;

    }

    @Override
    public boolean delete(T t)
    {
        if (t != null && t.getIdBase() != 0)
        {
            // Booleen permettant de créer ou non une transaction SQL
            boolean test = em.getTransaction().isActive();

            if (!test)
                em.getTransaction().begin();

            em.remove(t);

            if (!test)
                em.getTransaction().commit();

            return true;
        }
        return false;

    }

    @Override
    public final int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery(modele.getSimpleName() + RESET).executeUpdate();
        em.createNativeQuery(ALTERTABLE + nomTable + AUTOINC).executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PROTECTED ----------*/

    /**
     * Méthode interne de persistance pour enregistrer les sous objets de la classe.
     * 
     * @param classObjet
     *                   La classe de l'objet à persister.
     * @param objet
     *                   L'objet à persister.
     * @param            <O>
     *                   la classe de l'objet à persister. hérite de {@link model.bdd.AbstractBDDModele}.
     */
    protected <O extends AbstractBDDModele<J>, J> void persistSousObjet(Class<O> classObjet, O objet)
    {
        if (objet != null)
        {
            if (objet.getIdBase() == 0)
                DaoFactory.getMySQLDao(classObjet).persist(objet);
            else
                em.merge(objet);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings("unchecked")
    private void buildDao()
    {
        // Permet de récupérer la classe sous forme de type parametre
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = Statics.PATTERNSPACE.split(pt.getActualTypeArguments()[0].toString())[1];

        // Instanciation du paramètre avec la bonne classe
        try
        {
            modele = (Class<T>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new TechnicalException("Impossible d'instancier l'énumération - control.excel.ControlExcelRead", e);
        }
    }

    /*---------- ACCESSEURS ----------*/
}

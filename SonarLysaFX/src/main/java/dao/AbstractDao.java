package dao;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.ModelFactory;
import model.bdd.AbstractBDDModele;
import model.bdd.DateMaj;
import model.enums.TypeDonnee;
import utilities.TechnicalException;

/**
 * Classe abstraite de création de l'Entity Manager Toutes les classes de DAO vont hériter de cette classe
 * 
 * @author Tiggy Tamal
 *
 * @param <T>
 *            Correspond à la classe de modèle que l'on va gérer
 */
public abstract class AbstractDao<T extends AbstractBDDModele>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger de debug console */
    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SonarLysaFX");
    protected EntityManager em;
    protected TypeDonnee typeDonnee;
    private Class<T> modele;

    /*---------- CONSTRUCTEURS ----------*/

    @SuppressWarnings("unchecked")
    public AbstractDao()
    {
        em = emf.createEntityManager();

        // Permet de récuperer la classe sous forme de type paramétré
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // Instanciation du paramètre avec la bonne classe
        try
        {
            modele = (Class<T>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'énumération - control.excel.ControlExcelRead", e);
        }
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Extrait les données depuis un fichier Excel et les persiste en base.
     * 
     * @param file
     * @return
     */
    public abstract int recupDonneesDepuisExcel(File file);

    /**
     * Remise à zéro de la table et de l'indice de clef primaire.
     * 
     * @return
     */
    public abstract int resetTable();

    /**
     * Récupère un élément de la base de donnée selon son code.
     * 
     * @param code
     * @return
     */
    public abstract T recupEltParCode(String code);

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Retourne toutes les données de la table sous forme d'une liste
     * 
     * @return
     */
    public final List<T> readAll()
    {
        // Appel de la requête et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        List<T> retour = em.createNamedQuery(modele.getSimpleName() + AbstractBDDModele.FINDALL, modele).getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requête ReadAll " + modele + " = " + (fin - debut) + " ms");

        return retour;
    }

    /**
     * Retourne une map de toutes les données en map avec une clef dépendant de la classe
     * 
     * @return
     */
    public final Map<String, T> readAllMap()
    {
        Map<String, T> retour = new HashMap<>();

        for (T t : readAll())
        {
            retour.put(t.getMapIndex(), t);
        }
        return retour;
    }

    /**
     * Méthode de sauvegarde d'une collection d'éléments.<br/>
     * Retourne le nombre d'éléments enregistrés.
     * 
     * @param collection
     * @return
     */
    public final int persist(Iterable<T> collection)
    {
        em.getTransaction().begin();
        int i = 0;
        for (T t : collection)
        {
            persist(t);
            i++;
        }
        em.getTransaction().commit();
        return i;
    }

    /**
     * Méthode de sauvegarde d'un élément.<br/>
     * Retourne vrai si l'objet a bien été persisté, faux pour un merge
     * 
     * @param t
     */
    public boolean persist(T t)
    {
        boolean test = em.getTransaction().isActive();
        boolean retour = false;

        if (!test)
            em.getTransaction().begin();

        if (t.getIdBase() == 0)
        {
            em.persist(t);
            retour = true;
        }
        else
            em.merge(t);
        if (!test)
            em.getTransaction().commit();
        return retour;
    }

    /**
     * Mise à jour de la date de modification de la table
     * 
     * @param typeDonnee
     *            Type de donnée mise à jour
     * @return
     */
    public final boolean majDateDonnee()
    {
        DaoDateMaj dao = DaoFactory.getDao(DateMaj.class);
        DateMaj dateMaj = dao.recupEltParCode(typeDonnee.toString());
        if (dateMaj == null)
        {
            dateMaj = ModelFactory.getModel(DateMaj.class);
            dateMaj.setTypeDonnee(typeDonnee);
        }
        dateMaj.setDate(LocalDate.now());
        return dao.persist(dateMaj);
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/
}

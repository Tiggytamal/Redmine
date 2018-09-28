package dao;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.bdd.AbstractBDDModele;
import model.bdd.LotRTC;
import utilities.TechnicalException;

/**
 * Classe abstraite de cr�ation de l'Entity Manager Toutes les classes de DAO vont h�riter de cette classe
 * 
 * @author Tiggy Tamal
 *
 * @param <T>
 *            Correspond � la classe de mod�le que l'on va g�rer
 */
public abstract class AbstractDao<T extends AbstractBDDModele>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger de debug console */
    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
    private static EntityManagerFactory emf= Persistence.createEntityManagerFactory("SonarLysaFX");
    protected EntityManager em;
    private Class<T> modele;

    /*---------- CONSTRUCTEURS ----------*/

    public AbstractDao()
    {
        em = emf.createEntityManager();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Extrait les donn�es depuis un fichier Excel et les persiste en base.
     * 
     * @param file
     * @return
     */
    public abstract int recupDonneesDepuisExcel(File file);

    /**
     * Retourne une liste de toutes les donn�es en base
     * 
     * @return
     */
    public abstract List<T> readAll();

    /**
     * Remise � z�ro de la table et de l'indice de clef primaire.
     * 
     * @return
     */
    public abstract int resetTable();
    
    /**
     * R�cup�re un �l�ment de la base de donn�e selon son code.
     * @param code
     * @return
     */
    public abstract T recupEltParCode(String code);

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Retourne une map de toutes les donn�es en map avec une clef d�pendant de la classe
     * 
     * @return
     */
    public final Map<String, T> readAllMap()
    {
        Map<String, T> retour = new HashMap<>();

        for (T t : readAllcache())
        {
            retour.put(t.getMapIndex(), t);
        }
        return retour;
    }
    
    /**
     * M�thode de sauvegarde d'un �l�ment.<br/>
     * Retourne vrai si l'objet a bien �t� persist�, faux pour un merge
     * 
     * @param t
     */
    public boolean persist(T t)
    {
        if (t.getIdBase() == 0)
        {
            em.persist(t);
            return true;
        }
        else
            em.merge(t);
        return false;
    }

    /**
     * M�thode de sauvegarde d'une collection d'�l�ments.<br/>
     * Retourne le nombre d'�l�ments enregistr�s.
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

    /*---------- METHODES PRIVEES ----------*/
    
    @SuppressWarnings("unchecked")
    private List<T> readAllcache()
    {
        long debut = System.nanoTime();
        
        // Permet de r�cuperer la classe sous forme de type param�tr�
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        
        // On r�cup�re les param�tres de classe (ici T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        
        // Instanciation du param�tre avec la bonne classe
        try
        {
            modele = (Class<T>) Class.forName(parameterClassName);
        } 
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'�num�ration - control.excel.ControlExcelRead", e);
        }
        
        long middle = System.nanoTime();
        
        modele = (Class<T>) LotRTC.class;
        
        long fin = System.nanoTime();
        
        List<T> retour = em.createNamedQuery(modele.getSimpleName() +  ".findAll", modele).getResultList();
        

        
        // Affichage des temps de traitement
        LOGCONSOLE.info("instanciation = " + (middle - debut) + " - temps requ�te = " + (fin - middle));
        
        return retour;
        
    }
    /*---------- ACCESSEURS ----------*/
}

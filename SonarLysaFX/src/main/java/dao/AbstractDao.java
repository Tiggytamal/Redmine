package dao;

import java.io.File;
import java.io.Serializable;
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
 * Classe abstraite de cr�ation de l'Entity Manager Toutes les classes de DAO vont h�riter de cette classe
 * 
 * @author Tiggy Tamal
 *
 * @param <T>
 *            Correspond � la classe de mod�le que l'on va g�rer
 */
public abstract class AbstractDao<T extends AbstractBDDModele> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /** logger de debug console */
    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    /** Entitymanager */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SonarLysaFX");

    /** Constantes de requ�tes */
    public static final String FINDALL = ".findAll";
    public static final String FINDINDEX = ".findByIndex";
    public static final String RESET = ".resetTable";
    protected static final String INDEX = "index";
    protected static final String ALTERTABLE = "ALTER TABLE ";
    protected static final String AUTOINC = " AUTO_INCREMENT = 0";

    protected transient EntityManager em;
    protected TypeDonnee typeDonnee;
    protected Class<T> modele;
    protected String nomTable;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractDao(String nomTable)
    {
        this.nomTable = nomTable;
        em = emf.createEntityManager();
        buildDao();
    }

    protected AbstractDao(String nomTable, EntityManager em)
    {
        this.nomTable = nomTable;
        this.em = em;
        buildDao();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Extrait les donn�es depuis un fichier Excel et les persiste en base.
     * 
     * @param file
     * @return
     */
    public abstract int recupDonneesDepuisExcel(File file);

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Retourne toutes les donn�es de la table sous forme d'une liste
     * 
     * @return
     */
    public final List<T> readAll()
    {
        // Appel de la requ�te et calcul du temps de traitement
        long debut = System.currentTimeMillis();
        List<T> retour = em.createNamedQuery(modele.getSimpleName() + FINDALL, modele).getResultList();
        long fin = System.currentTimeMillis();

        // Affichage des temps de traitement
        LOGCONSOLE.info("temps requ�te ReadAll " + modele + " = " + (fin - debut) + " ms");

        return retour;
    }

    /**
     * Retourne une map de toutes les donn�es en map avec une clef d�pendant de la classe
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
     * R�cup�re un �l�ment de la base de donn�e selon son code.
     * 
     * @param code
     * @return
     */
    public T recupEltParIndex(String index)
    {
        List<T> liste = em.createNamedQuery(modele.getSimpleName() + FINDINDEX, modele).setParameter(INDEX, index).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
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

    /**
     * M�thode de sauvegarde d'un �l�ment.<br/>
     * Retourne vrai si l'objet a bien �t� persist�, faux pour un merge
     * 
     * @param t
     */
    public final boolean persist(T t)
    {
        boolean test = em.getTransaction().isActive();
        if (!test)
            em.getTransaction().begin();
        boolean retour = persistImpl(t);
        if (!test)
            em.getTransaction().commit();
        return retour;
    }

    /**
     * Suppression d'une liste d'�l�ments de la base de donn�es. Retourne le nombre d'�l�ments supprim�s
     * 
     * @param collection
     * @return
     */
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

    /**
     * Supprime un �l�ment de la base de donn�es
     * 
     * @param t
     */
    public void delete(T t)
    {
        if (t.getIdBase() != 0)
            em.remove(t);
    }

    /**
     * Mise � jour de la date de modification de la table
     * 
     * @param typeDonnee
     *            Type de donn�e mise � jour
     * @return
     */
    public final boolean majDateDonnee()
    {
        DaoDateMaj dao = DaoFactory.getDao(DateMaj.class);
        DateMaj dateMaj = dao.recupEltParIndex(typeDonnee.toString());
        if (dateMaj == null)
        {
            dateMaj = ModelFactory.getModel(DateMaj.class);
            dateMaj.setTypeDonnee(typeDonnee);
        }
        dateMaj.setDate(LocalDate.now());
        return dao.persist(dateMaj);
    }

    /**
     * Remise � z�ro de la table et de l'indice de clef primaire.
     * 
     * @return
     */
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
    
    protected <O extends AbstractBDDModele>void persistSousObjet(Class<O> classObjet, O objet)
    {
        if (objet != null)
        {
            if (objet.getIdBase() == 0)
                DaoFactory.getDao(classObjet).persist(objet);
            else
                em.merge(objet);
        }
    }
    
    protected boolean persistImpl(T t)
    {
        boolean retour = false;
        if (t.getIdBase() == 0)
        {
            t.initTimeStamp();
            em.persist(t);
            retour = true;
        }
        else
            em.merge(t);
        return retour;        
    }

    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings("unchecked")
    private void buildDao()
    {
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
    }

    /*---------- ACCESSEURS ----------*/
}

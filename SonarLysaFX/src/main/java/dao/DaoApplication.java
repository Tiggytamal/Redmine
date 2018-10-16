package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import control.excel.ControlApps;
import control.excel.ExcelFactory;
import model.bdd.Application;
import model.enums.TypeColApps;
import model.enums.TypeDonnee;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class DaoApplication extends AbstractDao<Application> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/

    DaoApplication()
    {
        typeDonnee = TypeDonnee.APPS;
    }
    
    DaoApplication(EntityManager em)
    {
        super(em);
        typeDonnee = TypeDonnee.APPS;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // R�cup�ration des donn�es du fichier Excel
        ControlApps control = ExcelFactory.getReader(TypeColApps.class, file);
        List<Application> mapExcel = control.recupDonneesDepuisExcel();

        // R�cup�ration des donn�es en base
        Map<String, Application> mapBase = readAllMap();

        // Mise � jour des applications d�j� enregistr�es et ajout des nouvelles.
        for (Application appli : mapExcel)
        {
            String key = appli.getMapIndex();
            if (mapBase.containsKey(key))
                mapBase.get(key).update(appli);
            else
                mapBase.put(key, appli);
        }

        // Persistance en base
        int retour = persist(mapBase.values());
        majDateDonnee();
        return retour;
    }

    @Override
    public Application recupEltParCode(String codeAppli)
    {
        List<Application> liste = em.createNamedQuery("Application.findByIndex", Application.class).setParameter("index", codeAppli).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    /**
     * Supprime tous les enregistrements de la base de la table des composants Sonar. Retourne le nombre d'enregistrements effac�s. Reset l'incr�mentation.
     * 
     * @return
     */
    @Override
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("Application.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

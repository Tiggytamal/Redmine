package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import control.excel.ControlApps;
import control.excel.ExcelFactory;
import model.bdd.Application;
import model.enums.TypeColApps;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoApplication extends AbstractDao<Application> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    
    DaoApplication() { }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlApps control = ExcelFactory.getReader(TypeColApps.class, file);
        Map<String, Application> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données en base
        Map<String, Application> mapBase = readAllMap();

        // Mise à jour des applications déjà enregistrées et ajout des nouvelles.
        for (Map.Entry<String, Application> entry : mapExcel.entrySet())
        {            
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        // Persistance en base
        return persist(mapBase.values());
    }
    
    @Override
    public List<Application> readAll()
    {
        return em.createNamedQuery("Application.findAll", Application.class).getResultList();
    }    

    @Override
    public Application recupEltParCode(String codeAppli)
    {
        List<Application> liste = em.createNamedQuery("Application.findByCode", Application.class).setParameter("code", codeAppli).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    /**
     * Supprime tous les enregistrements de la base de la table des composants Sonar. Retourne le nombre d'enregistrements effacés. Reset l'incrémentation.
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

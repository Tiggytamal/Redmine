package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import control.excel.ControlEdition;
import control.excel.ExcelFactory;
import model.bdd.Edition;
import model.enums.TypeColEdition;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoEdition extends AbstractDao<Edition> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    
    DaoEdition() { }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlEdition control = ExcelFactory.getReader(TypeColEdition.class, file);
        Map<String, Edition> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données en base
        Map<String, Edition> mapBase = readAllMap();

        // Mise à jour des données
        for (Map.Entry<String, Edition> entry : mapExcel.entrySet())
        {            
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        // PErsistance des données
        return persist(mapBase.values());
    }

    @Override
    public List<Edition> readAll()
    {
        return em.createNamedQuery("Edition.findAll", Edition.class).getResultList();
    }
    
    @Override
    public Edition recupEltParCode(String numero)
    {
        List<Edition> liste = em.createNamedQuery("Edition.findByCode", Edition.class).setParameter("code", numero).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    @Override
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("Edition.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

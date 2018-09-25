package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import control.excel.ControlChefService;
import control.excel.ExcelFactory;
import model.bdd.ChefService;
import model.enums.TypeColChefServ;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class DaoChefService extends AbstractDao<ChefService> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    
    DaoChefService() { }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // R�cup�ration des donn�es du fichier Excel
        ControlChefService control = ExcelFactory.getReader(TypeColChefServ.class, file);
        Map<String, ChefService> mapExcel = control.recupDonneesDepuisExcel();

        // R�cup�ration des donn�es en  base
        Map<String, ChefService> mapBase = readAllMap();

        // Mise � jour des donn�es avec celles du fichier Excel
        for (Map.Entry<String, ChefService> entry : mapExcel.entrySet())
        {            
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        // Persistance en base de donn�es
        return persist(mapBase.values());
    }

    @Override
    public List<ChefService> readAll()
    {
        return em.createNamedQuery("ChefService.findAll", ChefService.class).getResultList();
    }
    
    @Override
    public ChefService recupEltParCode(String nom)
    {
        List<ChefService> liste = em.createNamedQuery("ChefService.findByCode", ChefService.class).setParameter("code", nom).getResultList();
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
        retour = em.createNamedQuery("ChefService.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

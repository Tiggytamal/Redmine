package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import control.excel.ControlClarity;
import control.excel.ExcelFactory;
import model.bdd.ProjetClarity;
import model.enums.TypeColClarity;
import model.enums.TypeDonnee;

/**
 * Classe de DAO pour la sauvegarde des infoClarity en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoProjetClarity extends AbstractDao<ProjetClarity> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/

    DaoProjetClarity()
    {
        typeDonnee = TypeDonnee.CLARITY;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlClarity control = ExcelFactory.getReader(TypeColClarity.class, file);
        Map<String, ProjetClarity> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données depuis la base
        Map<String, ProjetClarity> mapBase = readAllMap();

        // Mise à jour des données
        for (Map.Entry<String, ProjetClarity> entry : mapExcel.entrySet())
        {
            if (mapBase.containsKey(entry.getKey()))
                mapBase.get(entry.getKey()).update(entry.getValue());
            else
                mapBase.put(entry.getKey(), entry.getValue());
        }

        // Persistance des données
        int retour = persist(mapBase.values());
        majDateDonnee();
        return retour;
    }

    @Override
    public boolean persist(ProjetClarity projet)
    {
        if (projet.getIdBase() == 0)
        {
            if (projet.getChefService().getIdBase() == 0)
                em.persist(projet.getChefService());
            em.persist(projet);
            return true;
        }
        em.merge(projet);
        return false;
    }

    @Override
    public ProjetClarity recupEltParCode(String codeClarity)
    {
        List<ProjetClarity> liste = em.createNamedQuery("ProjetClarity.findByIndex", ProjetClarity.class).setParameter("index", codeClarity).getResultList();
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
        retour = em.createNamedQuery("InfoClarity.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

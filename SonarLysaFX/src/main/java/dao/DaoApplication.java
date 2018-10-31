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
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoApplication extends AbstractDao<Application> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "applications";

    /*---------- CONSTRUCTEURS ----------*/

    DaoApplication()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.APPS;
    }

    DaoApplication(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.APPS;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlApps control = ExcelFactory.getReader(TypeColApps.class, file);
        List<Application> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données en base
        Map<String, Application> mapBase = readAllMap();

        // Mise à jour des applications déjà enregistrées et ajout des nouvelles.
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
    protected void persistImpl(Application t)
    {
        // Pas d'implémentation nécessaire       
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

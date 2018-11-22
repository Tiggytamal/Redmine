package dao;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import control.excel.ControlEdition;
import control.excel.ExcelFactory;
import model.bdd.Edition;
import model.enums.TypeColEdition;
import model.enums.TypeDonnee;
import utilities.Utilities;

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
    private static final String TABLE = "editions";

    /*---------- CONSTRUCTEURS ----------*/

    DaoEdition()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.EDITION;
    }

    DaoEdition(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.EDITION;
    }

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
            String keyExcel = entry.getKey();
            Edition edExcel = entry.getValue();
            if (mapBase.containsKey(keyExcel))
                mapBase.get(keyExcel).update(edExcel);
            else
                mapBase.put(keyExcel, edExcel);
        }

        // Persistance des données
        int retour = persist(mapBase.values());
        majDateDonnee();
        return retour;
    }

    @Override
    protected void persistImpl(Edition t)
    {
        // Pas d'implémentation nécessaire
    }
    
    public LocalDate recupDateEditionDepuisRepack(String nomRepack)
    {
        String numEd = Utilities.transcoEdition(nomRepack.substring(0, 3));
        String semaine = nomRepack.substring(nomRepack.length() - 2, nomRepack.length());
        List<LocalDate> result = em.createNamedQuery(modele.getSimpleName() + ".recupDateDepuisRepack", LocalDate.class).setParameter("numero", numEd+ "%").setParameter("semaine", "%" + semaine).getResultList();
        if (!result.isEmpty())
            return result.get(0);
        return null;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package dao;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;

import control.excel.ControlEdition;
import control.excel.ExcelFactory;
import model.bdd.Edition;
import model.enums.TypeColEdition;
import model.enums.TypeDonnee;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
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
        // R�cup�ration des donn�es du fichier Excel
        ControlEdition control = ExcelFactory.getReader(TypeColEdition.class, file);
        Map<String, Edition> mapExcel = control.recupDonneesDepuisExcel();

        // R�cup�ration des donn�es en base
        Map<String, Edition> mapBase = readAllMap();

        // Mise � jour des donn�es
        for (Map.Entry<String, Edition> entry : mapExcel.entrySet())
        {
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        // PErsistance des donn�es
        int retour = persist(mapBase.values());
        majDateDonnee();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

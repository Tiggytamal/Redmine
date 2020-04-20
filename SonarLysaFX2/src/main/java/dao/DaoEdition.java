package dao;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import control.excel.ControlEdition;
import control.excel.ExcelFactory;
import model.bdd.Edition;
import model.enums.ColEdition;
import utilities.Utilities;

/**
 * DAO pour la classe {@link model.bdd.Edition}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoEdition extends AbstractMySQLDao<Edition, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "editions";

    /*---------- CONSTRUCTEURS ----------*/

    DaoEdition()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlEdition control = ExcelFactory.getReader(ColEdition.class, file);
        Map<String, Edition> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données en base
        Map<String, Edition> mapBase = readAllMap();

        // Mise à jour des données
        for (Map.Entry<String, Edition> entry : mapExcel.entrySet())
        {
            String keyExcel = entry.getKey();
            Edition edExcel = entry.getValue();
            Edition edition = mapBase.get(keyExcel);
            if (edition != null)
                edition.update(edExcel);
            else
                mapBase.put(keyExcel, edExcel);
        }

        // Persistance des données
        return persist(mapBase.values());
    }

    @Override
    protected void persistImpl(Edition t)
    {
        // Pas d'implementation necessaire
    }

    /**
     * Permet de récupérer la date d'une edition depuis le nom de repack d'un composant
     * 
     * @param nomRepack
     *                  Nom du repack à chercher.
     * @return
     *         La date du repack trouvé ou null.
     */
    public LocalDate recupDateEditionDepuisRepack(String nomRepack)
    {
        // Rcupération de l'édition et de la semaine
        String numEd = Utilities.transcoEdition(nomRepack.substring(0, 3));
        String semaine = nomRepack.substring(nomRepack.length() - 2, nomRepack.length());

        // Protection code avec semaine 00
        if ("00".equals(semaine))
            semaine = "01";

        // Appel requête en base
        List<LocalDate> result = em.createNamedQuery(modele.getSimpleName() + ".recupDateDepuisRepack", LocalDate.class).setParameter("numero", numEd + "%").setParameter("semaine", "%" + semaine)
                .getResultList();
        if (!result.isEmpty())
            return result.get(0);
        return null;
    }

    public LocalDate recupDateEditionDepuisNumero(String idNgm)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".findByNumero", Edition.class).setParameter("numero", idNgm).getResultList().get(0).getDateMEP();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

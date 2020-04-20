package dao;

import java.io.File;
import java.util.Map;

import control.excel.ControlAppliDir;
import control.excel.ExcelFactory;
import model.bdd.Application;
import model.enums.ColAppliDir;

/**
 * DOA pour la classe {@link model.bdd.Application}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoApplication extends AbstractMySQLDao<Application, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "applications";

    /*---------- CONSTRUCTEURS ----------*/

    DaoApplication()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de récupérer les liens entre code application et département depuis un fichier Excel
     */
    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        ControlAppliDir control = ExcelFactory.getReader(ColAppliDir.class, file);
        Map<String, Application> map = control.recupDonneesDepuisExcel();
        return persist(map.values());
    }

    @Override
    protected void persistImpl(Application t)
    {
        // Pas d'implementation nécessaire
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

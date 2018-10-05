package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.ModelFactory;
import model.bdd.GroupementProjet;
import model.enums.GroupeProjet;
import model.enums.TypeColGrProjet;

/**
 * Classe de contrôle du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlGroupeProjets extends AbstractControlExcelRead<TypeColGrProjet, List<GroupementProjet>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colNom;
    private int colGroupe;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlGroupeProjets(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<GroupementProjet> recupDonneesDepuisExcel()
    {
        Sheet sheet = wb.getSheetAt(0);
        List<GroupementProjet> retour = new ArrayList<>();

        // Iterateur depuis la ligne 1 - sans les titres
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            GroupementProjet gp = ModelFactory.getModel(GroupementProjet.class);
            gp.setNomProjet(getCellStringValue(row, colNom));
            gp.setGroupe(GroupeProjet.valueOf(getCellStringValue(row, colGroupe)));
            retour.add(gp);
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

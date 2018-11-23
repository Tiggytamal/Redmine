package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.ModelFactory;
import model.bdd.Produit;
import model.enums.GroupeProduit;
import model.enums.TypeColProduit;

/**
 * Classe de contrôle du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlProduits extends AbstractControlExcelRead<TypeColProduit, List<Produit>>
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
    ControlProduits(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<Produit> recupDonneesDepuisExcel()
    {
        Sheet sheet = wb.getSheetAt(0);
        List<Produit> retour = new ArrayList<>();

        // Iterateur depuis la ligne 1 - sans les titres
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            Produit gp = ModelFactory.build(Produit.class);
            gp.setNomProjet(getCellStringValue(row, colNom));
            gp.setGroupe(GroupeProduit.valueOf(getCellStringValue(row, colGroupe)));
            retour.add(gp);
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package control.excel;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.LotSuiviPic;
import model.ModelFactory;
import model.enums.TypeColPic;

public final class ControlPic extends ControlExcelRead<TypeColPic, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    private int colLot;
    private int colLibelle;
    private int colClarity;
    private int colCpi;
    private int colEdition;
    private int colNbCompos;
    private int colNbpaquets;
    private int colBuild;
    private int colDevtu;
    private int colTfon;
    private int colVmoe;
    private int colVmoa;
    private int colLiv;

    /*---------- CONSTRUCTEURS ----------*/

    ControlPic(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de remonter tous les lots depuis l'extraction de l'outils de suivi Pic
     * 
     * @return
     */
    public Map<String, LotSuiviPic> recupDonneesDepuisExcel()
    {
        // Map de retour
        Map<String, LotSuiviPic> retour = new HashMap<>();

        // Itération sur toutes les feuilles du fichier Excel
        Iterator<Sheet> iter = wb.sheetIterator();
        while (iter.hasNext())
        {
            Sheet sheet = iter.next();

            // Itération sur chaque ligne pour récupérer les données
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) 
            {
                Row row = sheet.getRow(i);

                // Création de l'objet
                LotSuiviPic lot = ModelFactory.getModel(LotSuiviPic.class);
                lot.setLot(String.valueOf(getCellNumericValue(row, colLot)));
                lot.setLibelle(getCellStringValue(row, colLibelle));
                lot.setProjetClarity(getCellStringValue(row, colClarity));
                lot.setCpiProjet(getCellStringValue(row, colCpi));
                lot.setEdition(getCellStringValue(row, colEdition));
                lot.setNbreComposants(getCellNumericValue(row, colNbCompos));
                lot.setNbrePaquets(getCellNumericValue(row, colNbpaquets));
                lot.setBuild(getCellDateValue(row, colBuild));
                lot.setDevtu(getCellDateValue(row, colDevtu));
                lot.setTfon(getCellDateValue(row, colTfon));
                lot.setVmoe(getCellDateValue(row, colVmoe));
                lot.setVmoa(getCellDateValue(row, colVmoa));
                lot.setLivraison(getCellDateValue(row, colLiv));
                retour.put(lot.getLot(), lot);
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
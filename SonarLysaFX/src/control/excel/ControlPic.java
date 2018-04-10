package control.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.LotSuiviPic;
import model.ModelFactory;
import model.enums.TypeColPic;
import sonarapi.model.Vue;
import utilities.DateConvert;

public class ControlPic extends ControlExcel<TypeColPic, Map<String, LotSuiviPic>>
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

    public ControlPic(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de classer tous les lots Sonar du fichier dans une map. On enlève d'abord tout ceux qui ne sont pas présents dans SonarQube.<br>
     * Puis on les classes dans des listes, la clef de chaque liste correspond au mois et à l'année de mise en production du lot.<br>
     * Le fichier excel doit avoir un formattage spécifique, avec une colonne <b>Lot</b> (numérique) et un colonne <b>livraison édition</b> (date).<br>
     * 
     * @param file
     *            Le fichier excel envoyé par l'interface
     * @return
     * @throws FileNotFoundException
     * @throws EncryptedDocumentException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public Map<LocalDate, List<Vue>> recupLotsExcelPourMEP(Map<String, Vue> mapQube) throws IOException
    {
        // controle mapSonar
        if (mapQube == null || mapQube.isEmpty())
            throw new IllegalArgumentException("La liste des vues venant de Sonar est nulle ou vide - méthode control.excel.ControlPic.recupLotsExcelPourMEP");

        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);

        // Traitement Excel et contrôle avec SonarQube
        Map<LocalDate, List<Vue>> retour = creerMapVuesParMois(sheet, mapQube);

        write();
        close();
        return retour;
    }

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

    /**
     * @param sheet
     * @param wb
     * @param mapQube
     * @return
     */
    private Map<LocalDate, List<Vue>> creerMapVuesParMois(Sheet sheet, Map<String, Vue> mapQube)
    {
        // Initialisation de la map de retour
        Map<LocalDate, List<Vue>> retour = new HashMap<>();

        // parcours de la feuille Excel pour récupérer tous les lots et leurs dates de mise en production avec mise à jour du fichier Excel
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            traitementLigne(row, retour, mapQube);
        }
        return retour;
    }

    /**
     * Effectue le traitement de chaque ligne du fichier Excel
     * 
     * @param row
     *            Ligne du fichier Excel à traiter
     * @param colLot
     *            Indice de la colonne des numéros de lot
     * @param colDate
     *            Indice de la colonne des dates de livraison en production
     * @param retour
     *            Map retournant tous les lots à rajouter dans la vue
     * @param mapQube
     *            Map des vues retournées par SonarQube
     * @param wb
     *            Workbook
     */
    private void traitementLigne(Row row, Map<LocalDate, List<Vue>> retour, Map<String, Vue> mapQube)
    {
        Cell cellLot = row.getCell(colLot);
        Cell cellDate = row.getCell(colLiv);

        // Sortie si le numéro de lot ou la date de livraison ne sont pas remplis.
        if (cellLot.getCellTypeEnum() != CellType.NUMERIC && cellDate.getCellTypeEnum() != CellType.NUMERIC)
            return;

        String lot = String.valueOf((int) cellLot.getNumericCellValue());

        // On teste si le numéro de lot est bien présent dans Sonar.
        if (mapQube.keySet().contains(lot))
        {
            // Récupération de la date depuis le fichier Excel en format JDK 1.8.
            LocalDate date = DateConvert.localDate(cellDate.getDateCellValue());

            // Création d'une nouvelle date au 1er du mois qui servira du clef à la map.
            LocalDate clef = LocalDate.of(date.getYear(), date.getMonth(), 1);

            majCouleurLigne(row, IndexedColors.LIGHT_GREEN);

            // Mise à jour de la map
            if (!retour.keySet().contains(clef))
                retour.put(clef, new ArrayList<>());

            retour.get(clef).add(mapQube.get(lot));
        }
        else if (getCellNumericValue(row, colNbCompos) > 0)
            majCouleurLigne(row, IndexedColors.LIGHT_YELLOW);
    }
    /*---------- ACCESSEURS ----------*/

    @Override
    protected void initEnum()
    {
        enumeration = TypeColPic.class;
    }
}
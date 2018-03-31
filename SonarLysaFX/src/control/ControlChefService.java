package control;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.parent.ControlExcel;
import model.RespService;
import model.enums.TypeCol;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class ControlChefService extends ControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    // Noms des colonnes
    private String filiere;
    private String direction;
    private String departement;
    private String service;
    private String manager;

    // Indices des colonnes
    private int colFil;
    private int colDir;
    private int colDepart;
    private int colService;
    private int colManager;

    private static final int NOMBRECOL = 5;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public ControlChefService(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, RespService> recupRespDepuisExcel()
    {
        // Liste de retour
        Map<String, RespService> retour = new HashMap<>();

        // Itération sur toutes les feuilles du fichier Excel
        Sheet sheet = wb.getSheetAt(0);

        // Itération sur chaque ligne pour récupérer les données
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);

            // Création de l'objet
            RespService respServ = new RespService();
            respServ.setDepartement(getCellStringValue(row, colDepart));
            respServ.setDirection(getCellStringValue(row, colDir));
            respServ.setService(getCellStringValue(row, colService));
            respServ.setFiliere(getCellStringValue(row, colFil));
            respServ.setNom(getCellStringValue(row, colManager));
            retour.put(respServ.getService(), respServ);
        }
        
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    
    @Override
    protected void calculIndiceColonnes()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier est vide");

        titres = sheet.getRow(0);
        int nbreCol = 0;

        // Récupération des indices de colonnes
        for (Cell cell : titres)
        {
            if (cell.getCellTypeEnum() != CellType.STRING)
                continue;

            if (cell.getStringCellValue().equals(direction))
            {
                colDir = cell.getColumnIndex();
                testMax(colDir);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(departement))
            {
                colDepart = cell.getColumnIndex();
                testMax(colDepart);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(service))
            {
                colService = cell.getColumnIndex();
                testMax(colService);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(manager))
            {
                colManager = cell.getColumnIndex();
                testMax(colManager);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(filiere))
            {
                colFil = cell.getColumnIndex();
                testMax(colFil);
                nbreCol++;
            }
        }
        if (nbreCol != NOMBRECOL)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier excel est mal configuré, vérifié les colonnes de celui-ci");
    }

    @Override
    protected void initColonnes()
    {
        Map<TypeCol, String> nomColonnes = proprietesXML.getMapColonnes();
        direction = nomColonnes.get(TypeCol.DIRECTION);
        departement = nomColonnes.get(TypeCol.DEPARTEMENT);
        service = nomColonnes.get(TypeCol.SERVICE);
        manager = nomColonnes.get(TypeCol.MANAGER);
        filiere = nomColonnes.get(TypeCol.FILIERE);
    }
    
    /*---------- ACCESSEURS ----------*/
}

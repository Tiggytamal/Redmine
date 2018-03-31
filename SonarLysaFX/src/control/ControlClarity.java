package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.parent.ControlExcel;
import model.InfoClarity;

public class ControlClarity extends ControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    private int colActif;
    private int colClarity;
    private int colLib;
    private int colCpi;
    private int colEdition;
    private int colDir;
    private int colDepart;
    private int colService;
    private List<Integer> cols;

    // Liste des noms de colonnes
    private static final String ACTIF = "Actif";
    private static final String CLARITY = "Code projet";
    private static final String LIBELLE = "Libellé projet";
    private static final String CPI = "Chef de projet";
    private static final String EDITION = "Edition";
    private static final String DIRECTION = "Direction";
    private static final String DEPARTEMENT = "Département";
    private static final String SERVICE = "Service";
    /*---------- CONSTRUCTEURS ----------*/

    public ControlClarity(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, InfoClarity> recupInfosClarityExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);

        Map<String, InfoClarity> retour = new HashMap<>();
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            if (row.getCell(colClarity) != null)
            {
                InfoClarity info = traitementLigne(row);
                retour.put(info.getCodeClarity(), info);
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void initColonnes()
    {
        // pas implémenté encore       
    }
    
    @Override
    protected void calculIndiceColonnes()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);
        cols = new ArrayList<>();

        // Récupération des indices de colonnes
        for (Cell cell : sheet.getRow(0))
        {
            if (cell.getCellTypeEnum() == CellType.STRING)
            {
                switch (cell.getStringCellValue())
                {
                    case ACTIF:
                        colActif = cell.getColumnIndex();
                        cols.add(colActif);
                        break;
                    case DIRECTION:
                        colDir = cell.getColumnIndex();
                        cols.add(colDir);
                        break;
                    case DEPARTEMENT:
                        colDepart = cell.getColumnIndex();
                        cols.add(colDepart);
                        break;
                    case SERVICE:
                        colService = cell.getColumnIndex();
                        cols.add(colService);
                        break;
                    case CLARITY:
                        colClarity = cell.getColumnIndex();
                        cols.add(colClarity);
                        break;
                    case LIBELLE:
                        colLib = cell.getColumnIndex();
                        cols.add(colLib);
                        break;
                    case CPI:
                        colCpi = cell.getColumnIndex();
                        cols.add(colCpi);
                        break;
                    case EDITION:
                        colEdition = cell.getColumnIndex();
                        cols.add(colEdition);
                        break;
                    default:
                        // Colonne sans nom reconnu
                        break;
                }
            }
        }
    }
    
    /**
     * Traitement d'une ligne pour créer un objet InfoClarity
     * 
     * @param row
     * @return
     */
    private InfoClarity traitementLigne(Row row)
    {
        // Création de l'objet de retour
        InfoClarity info = new InfoClarity();

        // Itération sur les colonnes initialisées
        for (Integer col : cols)
        {
            Cell cell = row.getCell(col);

            // Si la valeur n'est pas de type String, on passe à la donnée suivante.
            if (cell == null || cell.getCellTypeEnum() != CellType.STRING)
                continue;

            // Récupération de la donnée et valorisation du retour selon la colonne
            String value = cell.getStringCellValue();
            traitementCellule(value, col, info);
        }
        return info;
    }

    /**
     * Traitement d'un cellule pour valoriser une donnée selon la colonne
     * 
     * @param value
     * @param col
     * @param info
     */
    private void traitementCellule(String value, Integer col, InfoClarity info)
    {
        if (col == colActif)
        {
            if ("Oui".equals(value))
                info.setActif(true);
            else
                info.setActif(false);
        }
        else if (col == colClarity)
            info.setCodeClarity(value);
        else if (col == colLib)
            info.setLibelleProjet(value);
        else if (col == colCpi)
            info.setChefProjet(value);
        else if (col == colEdition)
            info.setEdition(value);
        else if (col == colDir)
            info.setDirection(value);
        else if (col == colDepart)
            info.setDepartement(value);
        else if (col == colService)
            info.setService(value);
    }

    /*---------- ACCESSEURS ----------*/

}
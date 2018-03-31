package control;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.parent.ControlExcel;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class ControlEdition extends ControlExcel
{
    /*---------- ATTRIBUTS ----------*/
    private int colVersion;
    private int colLib;

    private static final String LIBELLE = "Libellé";
    private static final String VERSION = "Numero de version";
    private static final int NBRECOLONNE = 2;
    private static final String CHC = "CHC";
    private static final String CDM = "CDM";

    /*---------- CONSTRUCTEURS ----------*/

    public ControlEdition(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * 
     * @return
     */
    public Map<String,Map<String, String>> recupEditionDepuisExcel(List<String> annees)
    {
        //Initialisation map
        Map<String,Map<String, String>> retour = new HashMap<>();
        Map<String, String> chc = new HashMap<>();
        Map<String, String> cdm = new HashMap<>();
        retour.put("CHC", chc);
        retour.put("CDM", cdm);
        
        
        Sheet sheet = wb.getSheetAt(0);
        for (int i =1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            for (String annee : annees)
            {
                if (getCellStringValue(row, colLib).contains(CDM + annee))
                {
                    cdm.put(getCellFormulaValue(row, colVersion), prepareLibelle(getCellStringValue(row, colLib), true));
                }
                else if (getCellStringValue(row, colLib).contains(CHC + annee))
                {
                    chc.put(getCellFormulaValue(row, colVersion), prepareLibelle(getCellStringValue(row, colLib), false));
                }
            }
        }
        return retour;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    @Override
    protected void calculIndiceColonnes()
    {
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        int nbre = 0;

        for (int i = 0; i < row.getLastCellNum() + 1; i++)
        {
            if (LIBELLE.equals(getCellStringValue(row, i)))
            {
                colLib = i;
                nbre++;
            }
            else if (getCellStringValue(row, i).contains(VERSION))
            {
                colVersion = i;
                nbre++;
            }
            if (nbre == 2)
                break;
        }
        
        if (nbre != NBRECOLONNE)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier excel est mal configuré (" + file.getName() + " , vérifier les colonnes de celui-ci");

    }

    @Override
    protected void initColonnes()
    {
        // Pas besoin d'initialiser les colonnes
    }
    
    private String prepareLibelle(String libelle, boolean cdm)
    {
        String[] split = libelle.split("/");
        if (cdm && split.length == 2)
        {
            String retour = split[1].trim();
            if (retour.matches("^CDM20[12][0-9]\\-S[0-5][0-9]$"))
                return "CHC_" + retour;
        }
        else
        {
            String retour = split[0].trim();
            if (retour.matches("^CHC20[12][0-9]\\-S[0-5][0-9]$"))
                return retour;
        }
        throw new FunctionalException(Severity.SEVERITY_ERROR, "Mauvais format d'une edition du fichier Excel - libelle " + libelle);
    }
    
    /*---------- ACCESSEURS ----------*/
}
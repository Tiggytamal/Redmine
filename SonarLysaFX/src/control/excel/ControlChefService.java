package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.ModelFactory;
import model.RespService;
import model.enums.TypeColChefServ;

public class ControlChefService extends ControlExcel<TypeColChefServ, Map<String, RespService>>
{
    /*---------- ATTRIBUTS ----------*/

    // Indices des colonnes
    private int colFil;
    private int colDir;
    private int colDepart;
    private int colService;
    private int colManager;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public ControlChefService(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, RespService> recupDonneesDepuisExcel()
    {
        // Liste de retour
        Map<String, RespService> retour = new HashMap<>();

        // It�ration sur toutes les feuilles du fichier Excel
        Sheet sheet = wb.getSheetAt(0);

        // It�ration sur chaque ligne pour r�cup�rer les donn�es
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);

            // Cr�ation de l'objet
            RespService respServ = ModelFactory.getModel(RespService.class);
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
    protected void initEnum()
    {
       enumeration = TypeColChefServ.class;       
    }
    
    /*---------- ACCESSEURS ----------*/
}
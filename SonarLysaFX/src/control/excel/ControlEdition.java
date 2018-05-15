package control.excel;

import static utilities.Statics.TODAY;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.TypeColEdition;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class ControlEdition extends ControlExcel<TypeColEdition, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colVersion;
    private int colLib;

    private static final String CHC = "CHC";
    private static final String CDM = "CDM";

    /*---------- CONSTRUCTEURS ----------*/

    public ControlEdition(File file) throws IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, String> recupDonneesDepuisExcel()
    {
        // Initialisation map
        Map<String, String> retour = new HashMap<>();

        Sheet sheet = wb.getSheetAt(0);

        int year = TODAY.getYear();
        List<String> annees = Arrays.asList(String.valueOf(year), String.valueOf(year + 1), String.valueOf(year - 1));

        // Itération sur toutes les lignes sauf la première. ON enregistre l'édition si le libelle correspond à une CHC
        // ou à une CHC_CDM
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            for (String annee : annees)
            {
                String libelle = getCellStringValue(row, colLib);
                if (libelle.contains(CDM + annee) || libelle.contains(CHC + annee))
                    retour.put(getCellFormulaValue(row, colVersion), prepareLibelle(getCellStringValue(row, colLib)));
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    private String prepareLibelle(String libelle)
    {
        // On découpe les libéllés avec /. Ensuite, on teste le format pour bien ne renvoyer que des CHC ou CDM.
        // Une teste d'abord pour les CDM puis les CHC en potition 1.
        String[] split = libelle.split("/");

        for (String string : split)
        {
            String retour = string.trim();
            if (retour.matches("^CDM20[12][0-9]\\-S[0-5][0-9]$"))
                return "CHC_" + retour;
        }

        String retour = split[0].trim();
        if (retour.matches("^CHC20[12][0-9]\\-S[0-5][0-9]$"))
            return retour;

        throw new FunctionalException(Severity.ERROR, "Mauvais format d'une edition du fichier Excel - libelle " + libelle);
    }

    @Override
    protected void initEnum()
    {
        enumeration = TypeColEdition.class;
    }

    /*---------- ACCESSEURS ----------*/
}
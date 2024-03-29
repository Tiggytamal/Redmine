package control.excel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.TypeColEdition;
import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * Classe de contr�le 
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class ControlEdition extends AbstractControlExcelRead<TypeColEdition, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colVersion;
    private int colLib;

    private static final String CHC = "CHC";
    private static final String CDM = "CDM";
    private final LocalDate today = LocalDate.now();
    
    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilit� par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contr�leur
     * @throws IOException
     *             Exception lors des acc�s lecture/�criture
     */
    ControlEdition(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, String> recupDonneesDepuisExcel()
    {
        // Initialisation map
        Map<String, String> retour = new HashMap<>();

        Sheet sheet = wb.getSheetAt(0);

        int year = today.getYear();
        List<String> annees = Arrays.asList(String.valueOf(year), String.valueOf(year + 1), String.valueOf(year - 1));

        // It�ration sur toutes les lignes sauf la premi�re. ON enregistre l'�dition si le libelle correspond � une CHC
        // ou � une CHC_CDM
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

    /**
     * Contr�le des libelle des �ditions dans le fichier.
     * 
     * @param libelle
     *  
     * @return
     * 
     */
    private String prepareLibelle(String libelle)
    {
        // On d�coupe les lib�ll�s avec /. Ensuite, on teste le format pour bien ne renvoyer que des CHC ou CDM.
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

    /*---------- ACCESSEURS ----------*/
}

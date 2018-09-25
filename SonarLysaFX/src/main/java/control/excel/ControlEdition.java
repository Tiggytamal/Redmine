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

import model.ModelFactory;
import model.bdd.Edition;
import model.enums.TypeColEdition;
import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * Classe de contrôle 
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlEdition extends AbstractControlExcelRead<TypeColEdition, Map<String, Edition>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String CHC = "CHC";
    private static final String CDM = "CDM";
    private final LocalDate today = LocalDate.now();
    
    private int colVersion;
    private int colLib;
    private int colComment;
    
    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlEdition(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, Edition> recupDonneesDepuisExcel()
    {
        // Initialisation map
        Map<String, Edition> retour = new HashMap<>();

        Sheet sheet = wb.getSheetAt(0);

        int year = today.getYear();
        List<String> annees = Arrays.asList(String.valueOf(year), String.valueOf(year + 1), String.valueOf(year - 1));

        // Itération sur toutes les lignes sauf la première. On enregistre l'édition si le libelle correspond à une CHC
        // ou à une CHC_CDM
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            for (String annee : annees)
            {
                String libelle = getCellStringValue(row, colLib);
                
                // ON vérfie qu'on a bien un libellé de version de type CHC et CDM
                if (libelle.contains(CDM + annee) || libelle.contains(CHC + annee))
                {
                    String nom = prepareLibelle(getCellStringValue(row, colLib));
                    String numero = getCellFormulaValue(row, colVersion);
                    Edition edition = ModelFactory.getModel(Edition.class);
                    edition.setNom(nom);
                    edition.setNumero(numero);
                    edition.setCommentaire(getCellStringValue(row, colComment));
                    retour.put(numero, edition);
                }
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Contrôle des libelle des éditions dans le fichier.
     * 
     * @param libelle
     *  
     * @return
     * 
     */
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

    /*---------- ACCESSEURS ----------*/
}

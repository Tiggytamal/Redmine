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
import model.enums.TypeEdition;
import utilities.TechnicalException;

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
    private static final String CDM = "CHC_CDM";
    private final LocalDate today = LocalDate.now();

    // indice des colonnes
    private int colVersion;
    private int colLib;
    private int colComment;
    private int colSemaine;

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
        List<String> annees = Arrays.asList(String.valueOf(year), String.valueOf(year + 1), String.valueOf(year - 1), String.valueOf(year - 2));

        // Itération sur toutes les lignes sauf la première.
        // On enregistre l'édition si le libelle correspond à une CHC ou à une CHC_CDM
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            for (String annee : annees)
            {
                String semaine = getCellStringValue(row, colSemaine);

                // On vérfie qu'on a bien une édition des annèes choisie
                if (semaine.contains(annee))
                {
                    Edition edition = ModelFactory.getModel(Edition.class);

                    String libelle = getCellStringValue(row, colLib);
                    String nom = prepareNomEdition(libelle);
                    edition.setNom(nom);

                    String numero = getCellFormulaValue(row, colVersion);
                    edition.setNumero(numero);


                    edition.setDateMEP(prepareDateMEP(semaine));

                    edition.setCommentaire(getCellStringValue(row, colComment));
                    retour.put(numero, edition);

                    edition.setTypeEdition(prepareTypeEdition(nom));
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
    private String prepareNomEdition(String libelle)
    {
        // On découpe les libéllés avec /.
        // Une teste d'abord pour les CDM puis les CHC en potition 1.
        String[] split = libelle.split("/");

        for (String string : split)
        {
            String retour = string.trim();
            if (retour.matches("^CDM20[12][0-9]\\-S[0-5][0-9]$"))
                return "CHC_" + retour;
            else if (retour.matches("^CDM20[12][0-9]\\-s[0-5][0-9]$"))
                return "CHC_" + retour.replace("s", "S"); 
            else if (retour.matches("^CDM20[12][0-9][0-5][0-9]$"))
                return "CHC_" + retour.substring(0, retour.length() - 2) + "-S" + retour.substring(retour.length() - 2);
        }

        String retour = split[0].trim();
        if (retour.matches("^CHC20[12][0-9]\\-S[0-5][0-9]$"))
            return retour;
        else if (retour.matches("^CHC20[12][0-9]\\-s[0-5][0-9]$"))
            return retour.replace("s", "S");
        else if (retour.matches("^CHC20[12][0-9][0-5][0-9]$"))
            return retour.substring(0, retour.length() - 2) + "-S" + retour.substring(retour.length() - 2);
        
        return libelle;
    }

    private LocalDate prepareDateMEP(String semaine)
    {
        if (semaine.contains("N/A") || semaine.contains("n/a") || semaine.isEmpty())
            return LocalDate.of(2099, 1, 1);

        if (!semaine.matches("^[Ss]\\d{2}\\s\\-\\s20[1-9]\\d$"))
            throw new TechnicalException("Erreur dans le fichier des éditions, information sur la semaine : " + semaine);

        String[] split = semaine.split(" - ");
        return LocalDate.ofYearDay(Integer.valueOf(split[1]), Integer.valueOf(split[0].substring(1)) * 7 + 1);
    }

    private TypeEdition prepareTypeEdition(String libelle)
    {
        if (libelle.startsWith(CDM))
            return TypeEdition.CDM;
        if (libelle.startsWith(CHC))
            return TypeEdition.CHC;
        return TypeEdition.AUTRE;
    }

    /*---------- ACCESSEURS ----------*/
}

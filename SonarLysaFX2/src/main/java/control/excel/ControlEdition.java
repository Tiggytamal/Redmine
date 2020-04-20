package control.excel;

import static utilities.Statics.SPACE;
import static utilities.Statics.TIRET;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;

import model.ModelFactory;
import model.bdd.Edition;
import model.enums.ColEdition;
import model.enums.TypeEdition;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de contrôle du fichier extrait depuis l'application des éditions
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlEdition extends AbstractControlExcelRead<ColEdition, Map<String, Edition>>
{
    /*---------- ATTRIBUTS ----------*/

    /** Rouge utilisé dans le fichier */
    private static final int[] ROUGE = new int[]
    { 221, 75, 57 };

    /** Vert utilisé dans le fichier */
    private static final int[] VERT = new int[]
    { 0, 166, 90 };

    private static final int RGB = 256;
    private static final Pattern PATTERNSEMAINE = Pattern.compile("20[1-2][0-9][0-5][0-9]");

    // indice des colonnes
    private int colNumero;
    private int colLib;
    private int colComment;
    private int colSemaine;
    private int colEdition;
    private int colType;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par défaut pour obliger l'utilisation de la factory
     * 
     * @param file
     *             Fichier qui sera traité par l'instance du contrôleur
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
        Sheet sheet = wb.getSheetAt(0);
        Map<String, Edition> retour = new HashMap<>((int) (sheet.getLastRowNum() * Statics.RATIOLOAD));

        // Itération sur toutes les lignes sauf la première
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            // Récupération des données
            Row row = sheet.getRow(i);
            String numero = getCellStringValue(row, colNumero);
            String libelle = getCellStringValue(row, colLib);
            String edtionMaj = getCellStringValue(row, colEdition);
            int semaine = getCellNumericValue(row, colSemaine);
            String type = getCellStringValue(row, colType);
            String comm = getCellStringValue(row, colComment);

            // Création de l'objet et valorisation des champs
            Edition edition = ModelFactory.build(Edition.class);

            edition.setNumero(numero);
            edition.setNom(libelle);
            edition.setEditionMajeure(edtionMaj);
            edition.setDateMEP(prepareDateMEP(semaine));
            edition.setTypeEdition(TypeEdition.getTypeEdition(type));
            edition.setCommentaire(comm);
            edition.setActif(calculActif(row.getCell(colLib)));
            retour.put(libelle, edition);
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de calculer si une édition est activée ou non. Pour cela, on utilise la couleur de la ligne. Il n'est pas possible de récupérer
     * l'information depuis l'extraction de l'application pour le moment.
     * 
     * @param cell
     *             La cellule à traiter.
     * @return
     *         Vrai si l'édition est actives.
     */
    private boolean calculActif(Cell cell)
    {
        // Récupération de de la police. On cast en XSSFFont pour avoir toues les méthodes nécessaires.
        XSSFFont font = (XSSFFont) wb.getFontAt(cell.getCellStyle().getFontIndexAsInt());

        // Couleurs rgb
        byte[] rgb = font.getXSSFColor().getRGB();
        int[] couleurRGB = new int[]
        { rgb[0] < 0 ? (rgb[0] + RGB) : rgb[0], rgb[1] < 0 ? (rgb[1] + RGB) : rgb[1], rgb[2] < 0 ? (rgb[2] + RGB) : rgb[2] };

        // Test si les 3 indices de couleurs correspondent au vert
        if (couleurRGB[0] == VERT[0] && couleurRGB[1] == VERT[1] && couleurRGB[2] == VERT[2])
            return true;

        // test si les 3 indices de couleur correspondent au rouge
        if (couleurRGB[0] == ROUGE[0] && couleurRGB[1] == ROUGE[1] && couleurRGB[2] == ROUGE[2])
            return false;

        // Retour erreur technique
        StringBuilder builder = new StringBuilder("control.excel.ControlEdition.calculActif - la couleur de la cellule est inconnue : ");
        builder.append(cell.getStringCellValue()).append(SPACE).append(TIRET).append(couleurRGB[0]).append(SPACE).append(TIRET).append(couleurRGB[1]).append(SPACE).append(TIRET).append(couleurRGB[2]);
        throw new TechnicalException(builder.toString());
    }

    /**
     * Mets en forme la date de MEP depuis le numéro de semaine.
     * 
     * @param semaine
     *                Numéro de la semaine.
     * @return
     *         La date au format LocalDate.
     */
    private LocalDate prepareDateMEP(int semaine)
    {
        String value = String.valueOf(semaine);
        if (!PATTERNSEMAINE.matcher(value).matches())
            return (Statics.DATEINCO2099);
        return LocalDate.ofYearDay(Integer.parseInt(value.substring(0, 4)), Integer.parseInt(value.substring(4, 6)) * 7);
    }

    /*---------- ACCESSEURS ----------*/
}

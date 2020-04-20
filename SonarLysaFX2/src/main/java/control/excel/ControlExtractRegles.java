package control.excel;

import java.io.File;
import java.util.Collection;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.task.AbstractTask;
import model.enums.Bordure;
import model.enums.ColRegle;
import model.rest.sonarapi.Activation;
import model.rest.sonarapi.Regle;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de ontrôle du fichier des extratcions des régles SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ControlExtractRegles extends AbstractControlExcelWrite<ColRegle, Collection<Regle>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SHEET = "Règles";
    private static final String VIRGULE = ", ";
    private static final int TAILLEHTML = 2000;

    private int colNom;
    private int colDesc;
    private int colKey;
    private int colLang;
    private int colSeverite;
    private int colTags;
    private int colType;
    private int colActivation;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par défaut pour obliger l'utilisation de la factory
     * 
     * @param file
     *             Fichier qui sera traité par l'instance du contrôleur
     */
    ControlExtractRegles(File file)
    {
        super(file);
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void creationExtraction(Collection<Regle> regles, AbstractTask task)
    {
        enregistrerDonnees(regles,  wb.getSheet(SHEET), task);
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected final void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        Sheet sheet = wb.createSheet(SHEET);
        Row row = sheet.createRow(0);

        for (ColRegle colRegle : ColRegle.values())
        {
            try
            {
                valoriserCellule(row, (Integer) getClass().getDeclaredField(colRegle.getNomCol()).get(this), centre, Statics.proprietesXML.getEnumMapColW(ColRegle.class).get(colRegle).getNom());
            }
            catch (IllegalAccessException | NoSuchFieldException e)
            {
                throw new TechnicalException("control.excel.ControlExtractVul - Impossible d'initialiser les colonnes du fichier.", e);
            }
        }

    }

    @Override
    protected void enregistrerDonnees(Collection<Regle> donnees, Sheet sheet, AbstractTask task)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle gauche = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.LEFT);
        centre.setWrapText(false);
        gauche.setWrapText(true);
        Row row;

        // Donnees affichage
        int i = 0;
        int size = donnees.size();

        for (Regle regle : donnees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);

            // Valeurs classiques
            valoriserCellule(row, colNom, centre, regle.getNom());
            if (regle.getHtmlDesc().length() > TAILLEHTML)
                valoriserCellule(row, colDesc, gauche, regle.getHtmlDesc().substring(0, TAILLEHTML));
            else
                valoriserCellule(row, colDesc, gauche, regle.getHtmlDesc());
            valoriserCellule(row, colKey, centre, regle.getKey());
            valoriserCellule(row, colLang, centre, regle.getNomLangage());
            valoriserCellule(row, colSeverite, centre, regle.getSeverite());
            valoriserCellule(row, colType, centre, regle.getType());

            // Liste des tags
            StringBuilder builder = new StringBuilder();
            for (String tags : regle.getSysTags())
            {
                builder.append(tags).append(VIRGULE);
            }
            if (builder.length() > 0)
                valoriserCellule(row, colTags, centre, builder.substring(0, builder.length() - 2));
            else
                valoriserCellule(row, colTags, centre, Statics.EMPTY);

            // liste des activations
            builder = new StringBuilder();
            for (Activation acti : regle.getActivations())
            {
                builder.append(acti.getqProfile()).append(Statics.SPACE).append(Statics.TIRET).append(acti.getSeverity()).append(Statics.NL);
            }
            valoriserCellule(row, colActivation, centre, builder.toString());

            // Affichage
            i++;
            if (task != null)
                task.updateProgress(i, size);
        }

        autosizeColumns(sheet);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

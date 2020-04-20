package control.excel;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.task.AbstractTask;
import model.bdd.ComposantBase;
import model.enums.Bordure;
import model.enums.ColCompo;
import utilities.DateHelper;
import utilities.Statics;

/**
 * Classe de contrôle du fichier d'extraction des composants
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlExtractCompo extends AbstractControlExcelWrite<ColCompo, Map<String, List<ComposantBase>>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String NOMSHEET = "Composants Sonar";
    private int colNom;
    private int colNouv;
    private int colVersion;
    private int colAnalyse;
    private int colQG;
    private int colNbreLigne;
    private int colN1;
    private int colQGN1;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par défaut pour obliger l'utilisation de la factory
     * 
     * @param file
     *             Fichier qui sera traité par l'instance du contrôleur
     */
    ControlExtractCompo(File file)
    {
        super(file);
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Création du fichier d'extraction deîos ma Map de composants données.
     * 
     * @param mapCompos
     *                  Map des composants rangés selon s'ils sont anciens ou nouveaux
     * @param task
     *                  Tâche parente. Peut-être nulle.
     */
    public void creerFichierExtraction(Map<String, List<ComposantBase>> mapCompos, AbstractTask task)
    {
        enregistrerDonnees(mapCompos, wb.getSheet(NOMSHEET), task);
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected final void initTitres()
    {
        // Création du style des titres
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);

        // Récupération de la feuille et création de la première ligne pour les titres
        Sheet sheet = wb.createSheet(NOMSHEET);
        Row row = sheet.createRow(0);

        // Valorisation des cellules en itérant sur l'énumération
        for (ColCompo typeColCompo : ColCompo.values())
        {
            valoriserCellule(row, getNumCol(typeColCompo), centre, Statics.proprietesXML.getEnumMapColW(ColCompo.class).get(typeColCompo).getNom());
        }
    }

    @Override
    protected void enregistrerDonnees(Map<String, List<ComposantBase>> mapCompos, Sheet sheet, AbstractTask task)
    {
        // Création du style des cellules.
        CellStyle style = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        style.setWrapText(false);

        for (List<ComposantBase> liste : mapCompos.values())
        {
            // Création de la nouvelle ligne
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            // Valorisation des infosrmations pour le composant le plus recent.
            ComposantBase compo = liste.get(0);
            valoriserCellule(row, colNom, style, compo.getNom());
            valoriserCellule(row, colNouv, style, compo.estNouveau());
            valoriserCellule(row, colVersion, style, compo.getVersion());
            String date = compo.getDerniereAnalyse() == null ? Statics.EMPTY : DateHelper.dateFrancais(compo.getDerniereAnalyse(), "YYYY/MM/dd - HH:mm");
            valoriserCellule(row, colAnalyse, style, date);
            valoriserCellule(row, colQG, style, compo.getQualityGate());
            valoriserCellule(row, colNbreLigne, style, compo.getLdc());

            if (liste.size() > 1)
            {
                // Valorisation pour le composant N-1
                ComposantBase compo1 = liste.get(1);
                valoriserCellule(row, colN1, style, compo1.getNom());
                valoriserCellule(row, colQGN1, style, compo1.getQualityGate());
            }
            else
            {
                valoriserCellule(row, colN1, style, Statics.EMPTY);
                valoriserCellule(row, colQGN1, style, Statics.EMPTY);
            }
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

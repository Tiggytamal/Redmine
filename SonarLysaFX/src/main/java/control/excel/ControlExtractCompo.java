package control.excel;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.bdd.ComposantSonar;
import model.enums.TypeColCompo;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Bordure;

/**
 * Classe de contrôle du fichier d'extraction des vulnérabilitès
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlExtractCompo extends AbstractControlExcelWrite<TypeColCompo, Map<TypeColCompo, List<ComposantSonar>>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String NOMSHEET = "Composants Sonar";
    private int colPat;
    private int colInco;
    private int colNonProd;
    private int colTermine;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlExtractCompo(File file)
    {
        super(file);
        calculIndiceColonnes();
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void ajouterExtraction(Map<TypeColCompo, List<ComposantSonar>> composSonar)
    {
        Sheet sheet = wb.getSheet(NOMSHEET);
        enregistrerDonnees(composSonar, sheet);
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected final void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);

        Sheet sheet = wb.createSheet(NOMSHEET);
        Row row = sheet.createRow(0);

        for (TypeColCompo typeColCompo : TypeColCompo.values())
        {
            valoriserCellule(row, getNumCol(typeColCompo), centre, Statics.proprietesXML.getEnumMapColW(TypeColCompo.class).get(typeColCompo).getNom());
        }
    }

    @Override
    protected void enregistrerDonnees(Map<TypeColCompo, List<ComposantSonar>> composSonar, Sheet sheet)
    {

        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        centre.setWrapText(false);

        for (Map.Entry<TypeColCompo, List<ComposantSonar>> entry : composSonar.entrySet())
        {
            int numCol = 0;

            switch (entry.getKey())
            {
                case INCONNU:
                    numCol = colInco;
                    break;

                case NONPROD:
                    numCol = colNonProd;
                    break;

                case PATRIMOINE:
                    numCol = colPat;
                    break;

                case TERMINE:
                    numCol = colTermine;
                    break;

                default:
                    throw new TechnicalException("control.excel.ControlExtractCompo.eregistrerDonnees - TypeColCompo Inconnu : " + entry.getKey());
            }

            for (int i = 0; i < entry.getValue().size(); i++)
            {
                valoriserCellule(getRow(i + 1, sheet), numCol, centre, entry.getValue().get(i).getNom());
            }
        }

        autosizeColumns(sheet);       
    }

    /*---------- METHODES PRIVEES ----------*/

    private Row getRow(int i, Sheet sheet)
    {
        Row row = sheet.getRow(i);
        if (row == null)
            row = sheet.createRow(i);
        return row;
    }

    /*---------- ACCESSEURS ----------*/
}

package control.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.DaoDefaultAppli;
import dao.DaoFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.bdd.DefaultAppli;
import model.bdd.DefaultQualite;
import model.enums.EtatDefault;
import model.fxml.DefaultQualiteFXML;
import utilities.CellHelper;
import utilities.Statics;
import utilities.enums.Bordure;

public class DefaultsQualiteControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private VBox box;
    @FXML
    private TableView<DefaultQualiteFXML> table;
    @FXML
    private Button extraire;
    @FXML
    private Button importer;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        List<DefaultQualiteFXML> listeFXML = new ArrayList<>();

        for (DefaultQualite dq : DaoFactory.getDao(DefaultQualite.class).readAll())
        {
            if (dq.getEtatDefault() == EtatDefault.CLOSE || dq.getEtatDefault() == EtatDefault.ABANDONNEE)
                listeFXML.add(new DefaultQualiteFXML(dq));
        }

        table.getItems().addAll(listeFXML);
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void afficher(ActionEvent event) throws IOException
    {
        // Pas de traitmetn sur cette feuillle pour le moment
    }

    @FXML
    public void extraire() throws InvalidFormatException, IOException
    {
        // Récupération non du fichier
        File file = saveFileFromFileChooser(TITRE);

        // Création wb et feuille
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Défaults Fermés");

        // Création des titres
        Row row = sheet.createRow(0);
        int cellIndex = 0;
        CellHelper helper = new CellHelper(wb);
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        CellStyle styleGris = helper.getStyle(IndexedColors.GREY_25_PERCENT);
        CellStyle styleBlanc = helper.getStyle(IndexedColors.GREY_25_PERCENT);

        for (TableColumn<DefaultQualiteFXML, ?> col : table.getColumns())
        {
            for (TableColumn<DefaultQualiteFXML, ?> col2 : col.getColumns())
            {
                Cell cell = row.createCell(cellIndex);
                cell.setCellValue(col2.getText());
                cell.setCellStyle(styleTitre);
                cellIndex++;
            }
        }

        // Ajout des données dans le tableau
        for (int i = 0; i < table.getItems().size(); i++)
        {
            row = sheet.createRow(i + 1);

            cellIndex = 0;
            for (TableColumn<DefaultQualiteFXML, ?> col : table.getColumns())
            {
                for (TableColumn<DefaultQualiteFXML, ?> col2 : col.getColumns())
                {
                    Cell cell = row.createCell(cellIndex);
                    if (row.getRowNum() % 2 == 0)
                        cell.setCellStyle(styleGris);
                    else
                        cell.setCellStyle(styleBlanc);
                    cell.setCellValue(col2.getCellData(i).toString());
                    cellIndex++;
                }
            }
        }

        // Autosize des colonnes
        for (int i = 0; i <= cellIndex; i++)
        {
            sheet.autoSizeColumn(i);
        }

        // Ecriture et fermeture des flux
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        wb.close();
        fileOut.close();
    }
    
    @FXML
    public void recupDonnesFichier() throws InvalidFormatException, IOException
    {
     // Récupération non du fichier
        File file = getFileFromFileChooser(TITRE);
        Workbook wb = WorkbookFactory.create(file);
        Sheet sheet = wb.getSheet("Composants avec pb. appli");
        DaoDefaultAppli dao = DaoFactory.getDao(DefaultAppli.class);
        Map<String, DefaultAppli> mapDefaultsAppli = dao.readAllMap();
        int i = 0;
        
        for(Iterator<Row> iter = sheet.rowIterator(); iter.hasNext();)
        {
            Row row = iter.next();
            
            // On saute les lignes qui n'ont pas de valeur à prendre ne compte
            if (row.getRowNum() == 0 && row.getCell(2).getStringCellValue().equals("A corriger") || row.getCell(3).getStringCellValue().equals(Statics.EMPTY))
                continue;
            
            String key = row.getCell(0).getStringCellValue();
            if (mapDefaultsAppli.containsKey(key))
            {
                mapDefaultsAppli.get(key).setAppliCorrigee(row.getCell(3).getStringCellValue());
                System.out.println(row.getCell(3).getStringCellValue());
                i++;
            }
            else
                System.out.println(key);           
        }
        dao.persist(mapDefaultsAppli.values());
        
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

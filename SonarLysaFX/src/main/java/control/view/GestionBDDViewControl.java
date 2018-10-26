package control.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import control.view.fxml.AbstractFXMLViewControl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.fxml.ModeleFXML;
import utilities.CellHelper;
import utilities.enums.Bordure;

/**
 * Controleur de la page de gestion de la base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class GestionBDDViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private VBox box;
    @FXML
    private Button extraire;
    @FXML
    private Button importer;
    @FXML
    private Button dq;
    @FXML
    private Button compo;
    @FXML
    private VBox tablePane;

    private AbstractFXMLViewControl controlleur;
    private ObservableList<Node> children;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize() throws IOException
    {
        children = backgroundPane.getChildren();

        // Initialisation de l'écran avec les défaults qualité
        Button button = new Button();
        button.idProperty().set("dq");
        afficher(new ActionEvent(button, tail -> null));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void afficher(ActionEvent event) throws IOException
    {
        Object source = event.getSource();
        if (children.size() > 1)
            children.remove(1);

        if (source instanceof Button)
        {
            String id = ((Button) source).getId();

            switch (id)
            {
                case "dq":
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DefaultQualiteBDD.fxml"));
                    VBox bddEq = loader.load();
                    backgroundPane.add(bddEq, 1, 0);
                    controlleur = loader.getController();
                    break;

                case "compo":

                    break;

                default:
                    break;
            }
        }
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
        CellStyle styleBlanc = helper.getStyle(IndexedColors.WHITE);

        TableView<ModeleFXML> table = controlleur.getTable();

        for (TableColumn<ModeleFXML, ?> col : table.getColumns())
        {
            for (TableColumn<ModeleFXML, ?> col2 : col.getColumns())
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
            for (TableColumn<ModeleFXML, ?> col : table.getColumns())
            {
                for (TableColumn<ModeleFXML, ?> col2 : col.getColumns())
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

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

package control.view.fxml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import dao.DaoDefautAppli;
import dao.DaoFactory;
import javafx.fxml.FXML;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.enums.EtatDefaut;
import model.enums.TypeAction;
import model.fxml.DefaultQualiteFXML;
import utilities.Statics;

public class DefaultQualiteBDDViewControl extends AbstractFXMLViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private List<DefaultQualiteFXML> listeFXML;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    @Override
    public void initialize()
    {
        listeFXML = new ArrayList<>();

        for (DefautQualite defaultQualite : DaoFactory.getDao(DefautQualite.class).readAll())
        {
            if (defaultQualite.getEtatDefaut() == EtatDefaut.CLOS || defaultQualite.getEtatDefaut() == EtatDefaut.ABANDONNE)
                listeFXML.add(new DefaultQualiteFXML(defaultQualite));
        }
        table.getItems().addAll(listeFXML);
    }

    @FXML
    public void recupDonnesFichier() throws InvalidFormatException, IOException
    {
        // Récupération non du fichier
        File file = getFileFromFileChooser("Fichier consolidation");
        Workbook wb = WorkbookFactory.create(file);
        Sheet sheet = wb.getSheet("Composants avec pb. appli");
        DaoDefautAppli daoDa = DaoFactory.getDao(DefautAppli.class);
        Map<String, DefautAppli> mapDefaultsAppli = daoDa.readAllMap();

        for (Iterator<Row> iter = sheet.rowIterator(); iter.hasNext();)
        {
            Row row = iter.next();
            String key = row.getCell(0).getStringCellValue();
            String action = row.getCell(2).getStringCellValue();
            String appliCorrigee = row.getCell(3).getStringCellValue();
            DefautAppli da = mapDefaultsAppli.get(key);

            // On saute la première ligne et toutes les lignes à vide qui ne sont pas à vérifier, ainsi que les composants qui ne sont plus dans SonarQube
            if (row.getRowNum() == 0 || da == null || (appliCorrigee.equals(Statics.EMPTY) && !row.getCell(2).getStringCellValue().equals(TypeAction.VERIFIER.getValeur())))
                continue;

            if (!appliCorrigee.isEmpty())
                da.setAppliCorrigee(appliCorrigee);
            else if (action.equals(TypeAction.VERIFIER.getValeur()))
            {
                da.setAppliCorrigee(row.getCell(1).getStringCellValue());
            }
        }
        daoDa.persist(mapDefaultsAppli.values());
        wb.close();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

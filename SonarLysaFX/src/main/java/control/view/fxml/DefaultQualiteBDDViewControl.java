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

import dao.DaoDefaultAppli;
import dao.DaoFactory;
import javafx.fxml.FXML;
import model.bdd.DefaultAppli;
import model.bdd.DefaultQualite;
import model.enums.EtatDefault;
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

        for (DefaultQualite defaultQualite : DaoFactory.getDao(DefaultQualite.class).readAll())
        {
            if (defaultQualite.getEtatDefault() == EtatDefault.CLOSE || defaultQualite.getEtatDefault() == EtatDefault.ABANDONNEE)
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
        DaoDefaultAppli dao = DaoFactory.getDao(DefaultAppli.class);
        Map<String, DefaultAppli> mapDefaultsAppli = dao.readAllMap();
        int i = 0;

        for (Iterator<Row> iter = sheet.rowIterator(); iter.hasNext();)
        {
            Row row = iter.next();
            String key = row.getCell(0).getStringCellValue();
            String action = row.getCell(2).getStringCellValue();
            String appliCorrigee = row.getCell(3).getStringCellValue();
            DefaultAppli da = mapDefaultsAppli.get(key);

            // On aute la première ligne et toutes les lignes à vide qui ne sont pas à vérifier, ainsi que les composants qui ne sont plus dans SonarQube
            if (row.getRowNum() == 0 || da == null || (appliCorrigee.equals(Statics.EMPTY) && !row.getCell(2).getStringCellValue().equals(TypeAction.VERIFIER.getValeur())))
                continue;

            if (!appliCorrigee.isEmpty())
                da.setAppliCorrigee(appliCorrigee);
            else if (action.equals(TypeAction.VERIFIER.getValeur()))
            {
                da.setAction(TypeAction.VERIFIER);
                da.setAppliCorrigee(row.getCell(1).getStringCellValue());
            }
            i++;
        }
        System.out.println(i);
        dao.persist(mapDefaultsAppli.values());
        wb.close();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

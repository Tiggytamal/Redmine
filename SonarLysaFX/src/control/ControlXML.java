package control;

import static control.view.MainScreen.fichiersXML;
import static control.view.MainScreen.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.Application;
import model.InfoClarity;
import model.LotSuiviPic;
import model.XML;
import model.enums.TypeFichier;
import utilities.TechnicalException;

/**
 * Classe de gestion des param�tres de XML de l'application
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public class ControlXML
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * R�cup�re le param�tre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * 
     * @throws JAXBException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public XML recuprerXML(Class<? extends XML> typeXML)
    {
        // variables
        JAXBContext context;
        XML retour;
        try
        {
            retour = typeXML.newInstance();
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new TechnicalException("Impossible d'instancier le fichier de param�tre", e);
        }
        
        File file = retour.getFile();
      
        try
        {
            context = JAXBContext.newInstance(typeXML);
            // R�cup�ration du param�trage depuis le fichier externe
            if (file.exists())
            {
                retour = (XML) context.createUnmarshaller().unmarshal(file);
            }
            
        } catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de r�cup�rer le fichier de param�tre, erreur JAXB", e);
        }
        
        return retour;
    }

    /**
     * Sauvegarde le fichier de param�tres
     * 
     * @throws JAXBException
     */
    public void saveParam(XML fichier) throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(fichier.getClass());
        Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(fichier, fichier.getFile());
    }

    /**
     * 
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     * @throws JAXBException
     */
    public void recupListeAppsDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
    {
        Workbook wb = WorkbookFactory.create(file);
        Sheet sheet = wb.getSheetAt(0);

        for (Row row : sheet)
        {
            Application app = new Application();
            Cell cell = row.getCell(0);

            if (cell.getCellTypeEnum() == CellType.STRING)
                app.setNom(row.getCell(0).getStringCellValue());
            else
                app.setNom(String.valueOf((int) row.getCell(0).getNumericCellValue()));

            String actif = row.getCell(1).getStringCellValue();
            if ("Actif".equals(actif))
                app.setActif(true);
            else
                app.setActif(false);

            fichiersXML.getListeApplications().add(app);
        }
        wb.close();
        fichiersXML.setDateFichier(TypeFichier.APPS);
        saveParam(fichiersXML);
    }

    public void recupInfosClarityDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
    {
        ControlClarity control = new ControlClarity(file);
        Map<String, InfoClarity> clarity = control.recupInfosClarityExcel();
        control.close();
        fichiersXML.getMapClarity().putAll(clarity);
        fichiersXML.setDateFichier(TypeFichier.CLARITY);
        saveParam(fichiersXML);
    }

    public void recupLotsPicDepuisExcel(File file) throws IOException, InvalidFormatException, JAXBException
    {
        ControlPic controlPic = new ControlPic(file);
        Map<String, LotSuiviPic> lotsPic = controlPic.recupLotsDepuisPic();
        controlPic.close();
        fichiersXML.getLotsPic().putAll(lotsPic);
        fichiersXML.setDateFichier(TypeFichier.LOTSPICS);
        saveParam(fichiersXML);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * 
     * @param texte
     */
    public void createAlert()
    {
        String texte = fichiersXML.controleDonnees() + proprietesXML.controleDonnees();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.initModality(Modality.NONE);
        alert.setContentText(texte);
        alert.setHeaderText(null);
        alert.show();
    }

    /*---------- ACCESSEURS ----------*/
}
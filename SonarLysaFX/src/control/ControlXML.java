package control;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import model.RespService;
import model.XML;
import model.enums.TypeFichier;
import utilities.TechnicalException;

/**
 * Classe de gestion des paramètres de XML de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class ControlXML
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * Récupère le paramètre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * 
     * @throws JAXBException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public XML recupererXML(Class<? extends XML> typeXML)
    {
        // variables
        JAXBContext context;
        XML retour;
        try
        {
            retour = typeXML.newInstance();
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new TechnicalException("Impossible d'instancier le fichier de paramètre", e);
        }
        
        File file = retour.getFile();
      
        try
        {
            context = JAXBContext.newInstance(typeXML);
            // Récupération du paramétrage depuis le fichier externe
            if (file.exists())
            {
                retour = (XML) context.createUnmarshaller().unmarshal(file);
            }
            
        } catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de récupérer le fichier de paramètre, erreur JAXB", e);
        }
        
        return retour;
    }

    /**
     * Sauvegarde le fichier de paramètres
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
        fichiersXML.getMapClarity().clear();
        fichiersXML.getMapClarity().putAll(clarity);
        fichiersXML.setDateFichier(TypeFichier.CLARITY);
        saveParam(fichiersXML);
    }

    public void recupLotsPicDepuisExcel(File file) throws IOException, InvalidFormatException, JAXBException
    {
        ControlPic control = new ControlPic(file);
        Map<String, LotSuiviPic> lotsPic = control.recupLotsDepuisPic();
        control.close();
        fichiersXML.getLotsPic().clear();
        fichiersXML.getLotsPic().putAll(lotsPic);
        fichiersXML.setDateFichier(TypeFichier.LOTSPICS);
        saveParam(fichiersXML);
    }
    
    public void recupChefServiceDepuisExcel(File file) throws IOException, InvalidFormatException, JAXBException
    {
        ControlChefService control = new ControlChefService(file);
        Map<String, RespService> respService = control.recupRespDepuisExcel();
        control.close();
        fichiersXML.getMapRespService().clear();
        fichiersXML.getMapRespService().putAll(respService);
        fichiersXML.setDateFichier(TypeFichier.RESPSERVICE);
        saveParam(fichiersXML);
    }
    
    public void recupEditionDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
    {
        ControlEdition control = new ControlEdition(file);
        List<String> liste = new ArrayList<>();
        liste.add("2018");
        Map<String, Map<String, String>> editions = control.recupEditionDepuisExcel(liste);
        control.close();
        fichiersXML.getMapCDM().clear();
        fichiersXML.getMapCDM().putAll(editions.get("CDM"));
        fichiersXML.getMapCHC().clear();
        fichiersXML.getMapCHC().putAll(editions.get("CHC"));
        fichiersXML.setDateFichier(TypeFichier.EDITION);
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
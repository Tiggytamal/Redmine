package control.xml;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.excel.ExcelFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.XML;
import model.enums.TypeCol;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeFichier;
import utilities.TechnicalException;

/**
 * Classe de gestion des param�tres de XML de l'application
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class ControlXML
{
    /*---------- ATTRIBUTS ----------*/

    private static final String ERREUR = "Erreur au moment de sauvegarder le fichier Excel";

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * R�cup�re le param�tre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JAXBException
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public <T extends XML> T recupererXML(Class<T> typeXML)
    {
        // variables
        JAXBContext context;
        T retour;
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
                retour = (T) context.createUnmarshaller().unmarshal(file);

        } catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de r�cup�rer le fichier de param�tre, erreur JAXB", e);
        }

        return retour;
    }
    
    /**
     * R�cup�re le param�tre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JAXBException
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public <T extends XML> T recupererXMLResources(Class<T> typeXML)
    {
        // variables
        JAXBContext context;
        T retour;
        try
        {
            retour = typeXML.newInstance();
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new TechnicalException("Impossible d'instancier le fichier de param�tre", e);
        }

        File file = retour.getResource();

        try
        {
            context = JAXBContext.newInstance(typeXML);
            
            // R�cup�ration du param�trage depuis le fichier externe
            if (file.exists())
                retour = (T) context.createUnmarshaller().unmarshal(file);

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
     * Enregistre le fichier Excel de la liste des applications dans les param�tres XML
     *
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     * @throws JAXBException
     */
    public void recupListeAppsDepuisExcel(File file)
    {
        saveInfos(TypeFichier.APPS, TypeColApps.class, file);
    }

    /**
     * Enregistre le fichier Excel des informations Clarity dans les param�tres XML
     * 
     * @param file
     */
    public void recupInfosClarityDepuisExcel(File file)
    {
        saveInfos(TypeFichier.CLARITY, TypeColClarity.class, file);
    }

    /**
     * Enregistre le fichier Excel des lots Pic dans les param�tres XML
     * 
     * @param file
     */
    public void recupLotsPicDepuisExcel(File file)
    {
        saveInfos(TypeFichier.LOTSPICS, TypeColPic.class, file);
    }

    /**
     * Enregistre le fichier Excel des chef de services dans les param�tres XML
     * 
     * @param file
     */
    public void recupChefServiceDepuisExcel(File file)
    {
        saveInfos(TypeFichier.RESPSERVICE, TypeColChefServ.class, file);
    }

    /**
     * R�cup�re depuis le fichier Excel toutes les �dition CHC/CDM, aver leurs num�ros de version, pour l'ann�e en cours, la pr�cedente et la suivante.
     * 
     * @param file
     */
    public void recupEditionDepuisExcel(File file)
    {
        saveInfos(TypeFichier.EDITION, TypeColEdition.class, file);
    }

    @SuppressWarnings("rawtypes")
    private <T extends Enum<T> & TypeCol> void saveInfos(TypeFichier typeFichier, Class<T> typeCol, File file)
    {
        try
        {
            fichiersXML.majMapDonnees(typeFichier, (Map) ExcelFactory.getControlleur(typeCol, file).recupDonneesDepuisExcel());            
            saveParam(fichiersXML);
        } catch (InvalidFormatException | IOException | JAXBException e)
        {
            throw new TechnicalException(ERREUR, e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
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
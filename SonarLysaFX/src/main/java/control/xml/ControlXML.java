package control.xml;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.excel.ExcelFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.enums.TypeColR;
import model.ModelFactory;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColNPC;
import model.enums.TypeFichier;
import model.utilities.AbstractModele;
import model.utilities.XML;
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

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    private static final short WIDTHALERT = 600;
    private static final short HEIGHTALERT = 360;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * R�cup�re le param�tre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @param typeXML
     *            Classe du param�tre � r�cup�rer
     * @return le fichier XML des�rialis�
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractModele & XML> T recupererXML(Class<T> typeXML)
    {
        // variables
        JAXBContext context;
        T retour = ModelFactory.getModel(typeXML);
        File file = retour.getFile();

        try
        {
            context = JAXBContext.newInstance(typeXML);

            // R�cup�ration du param�trage depuis le fichier externe
            if (file.exists())
                retour = (T) context.createUnmarshaller().unmarshal(file);

        }
        catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de r�cup�rer le fichier de param�tre, erreur JAXB", e);
        }

        return retour;
    }

    /**
     * R�cup�re le param�tre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @param typeXML
     *            Classe du param�tre � r�cup�rer
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractModele & XML> T recupererXMLResources(Class<T> typeXML)
    {
        // variables
        JAXBContext context;
        T retour = ModelFactory.getModel(typeXML);
        File file = retour.getResource();

        try
        {
            context = JAXBContext.newInstance(typeXML);

            // R�cup�ration du param�trage depuis le fichier externe
            if (file.exists())
                retour = (T) context.createUnmarshaller().unmarshal(file);

        }
        catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de r�cup�rer le fichier de param�tre, erreur JAXB", e);
        }

        return retour;
    }

    /**
     * Sauvegarde le fichier de param�tres. Retourne vrai si le fichier a bien �t� mis � jour.
     * 
     * @param fichier
     *            Fichier � suvagarder, doit impl�menter l'interface {@link model.utilities.XML}.
     * @throws JAXBException
     */
    public boolean saveParam(XML fichier)
    {
        try
        {
            long time = fichier.getFile().lastModified();
            JAXBContext context = JAXBContext.newInstance(fichier.getClass());
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(fichier, fichier.getFile());

            // Contr�le que le fichier a bien �t� mis � jour
            return time < fichier.getFile().lastModified();
        }
        catch (JAXBException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible de sauvegarder le fichier de propri�t�", e);
        }
    }

    /**
     * Enregistre le fichier Excel de la liste des applications dans les param�tres XML.
     * 
     * @param file
     *            Fichier � utiliser
     */
    public void recupListeAppsDepuisExcel(File file)
    {
        saveInfos(TypeFichier.APPS, TypeColApps.class, file);
    }

    /**
     * Enregistre le fichier Excel des informations Clarity dans les param�tres XML
     * 
     * @param file
     *            Fichier � utiliser
     */
    public void recupInfosClarityDepuisExcel(File file)
    {
        saveInfos(TypeFichier.CLARITY, TypeColClarity.class, file);
    }

    /**
     * Enregistre le fichier Excel des chef de services dans les param�tres XML
     * 
     * @param file
     *            Fichier � utiliser
     */
    public void recupChefServiceDepuisExcel(File file)
    {
        saveInfos(TypeFichier.RESPSERVICE, TypeColChefServ.class, file);
    }

    /**
     * R�cup�re depuis le fichier Excel toutes les �dition CHC/CDM, aver leurs num�ros de version, pour l'ann�e en cours, la pr�cedente et la suivante.
     * 
     * @param file
     *            Fichier � utiliser
     */
    public void recupEditionDepuisExcel(File file)
    {
        saveInfos(TypeFichier.EDITION, TypeColEdition.class, file);
    }

    /**
     * R�cup�re depuis le fichier Excel toutes les �dition CHC/CDM, aver leurs num�ros de version, pour l'ann�e en cours, la pr�cedente et la suivante.
     * 
     * @param file
     *            Fichier � utiliser
     */
    public void recupProjetsNPCDepuisExcel(File file)
    {
        saveInfos(TypeFichier.NPC, TypeColNPC.class, file);
    }

    /**
     * Sauvegarde les informations d'un fichier XML
     * 
     * @param typeFichier
     *            Type de fichier � suavegarder, �numaration {@link model.enums.TypeFichier}.
     * @param typeCol
     *            Type de colonnes de fichiers, �num�ration
     * @param file
     */
    @SuppressWarnings("rawtypes")
    private <T extends Enum<T> & TypeColR> void saveInfos(TypeFichier typeFichier, Class<T> typeCol, File file)
    {
        fichiersXML.majMapDonnees(typeFichier, (Map) ExcelFactory.getReader(typeCol, file).recupDonneesDepuisExcel());
        saveParam(fichiersXML);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Affichage alerte du controle des donn�es
     */
    public void createAlert()
    {
        String texte = fichiersXML.controleDonnees() + proprietesXML.controleDonnees();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add("application.css");

        alert.initStyle(StageStyle.UTILITY);
        alert.initModality(Modality.NONE);
        alert.setResizable(true);
        alert.setContentText(texte);
        alert.setHeaderText(null);
        alert.show();
        alert.setWidth(WIDTHALERT);
        alert.setHeight(HEIGHTALERT);
    }

    /*---------- ACCESSEURS ----------*/
}

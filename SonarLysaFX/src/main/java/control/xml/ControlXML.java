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
import model.ModelFactory;
import model.enums.TypeColNPC;
import model.enums.TypeColR;
import model.enums.TypeDonnee;
import model.utilities.AbstractModele;
import model.utilities.XML;
import utilities.TechnicalException;

/**
 * Classe de gestion des paramètres de XML de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
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
     * Récupère le paramètre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @param typeXML
     *            Classe du paramètre à récupérer
     * @return le fichier XML desérialisé
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

            // Récupération du paramétrage depuis le fichier externe
            if (file.exists())
                retour = (T) context.createUnmarshaller().unmarshal(file);

        }
        catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de récupérer le fichier de paramètre, erreur JAXB", e);
        }

        return retour;
    }

    /**
     * Récupère le paramètre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @param typeXML
     *            Classe du paramètre à récupérer
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

            // Récupération du paramétrage depuis le fichier externe
            if (file.exists())
                retour = (T) context.createUnmarshaller().unmarshal(file);

        }
        catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de récupérer le fichier de paramètre, erreur JAXB", e);
        }

        return retour;
    }

    /**
     * Sauvegarde le fichier de paramètres. Retourne vrai si le fichier a bien été mis à jour.
     * 
     * @param fichier
     *            Fichier à suvagarder, doit implémenter l'interface {@link model.utilities.XML}.
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

            // Contrôle que le fichier a bien été mis à jour
            return time < fichier.getFile().lastModified();
        }
        catch (JAXBException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible de sauvegarder le fichier de propriété", e);
        }
    }

    /**
     * Récupère depuis le fichier Excel toutes les édition CHC/CDM, aver leurs numéros de version, pour l'annèe en cours, la précedente et la suivante.
     * 
     * @param file
     *            Fichier à utiliser
     */
    public void recupProjetsNPCDepuisExcel(File file)
    {
        saveInfos(TypeDonnee.GROUPE, TypeColNPC.class, file);
    }

    /**
     * Sauvegarde les informations d'un fichier XML
     * 
     * @param typeFichier
     *            Type de fichier à suavegarder, énumaration {@link model.enums.TypeDonnee}.
     * @param typeCol
     *            Type de colonnes de fichiers, énumération
     * @param file
     */
    private <T extends Enum<T> & TypeColR> void saveInfos(TypeDonnee typeFichier, Class<T> typeCol, File file)
    {
        switch (typeFichier)
        {
                
            case GROUPE:
                fichiersXML.majMapDonnees(typeFichier, (Map) ExcelFactory.getReader(typeCol, file).recupDonneesDepuisExcel());
                saveParam(fichiersXML);
                break;
                
            default:
                throw new TechnicalException("control.xml.ControlXML.saveInfos - TypeFichier inconnu : " + typeFichier);
        }

    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Affichage alerte du controle des données
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

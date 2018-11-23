package control.xml;

import static utilities.Statics.proprietesXML;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.ModelFactory;
import model.interfaces.AbstractModele;
import model.interfaces.XML;
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
    private static final short WIDTHALERT = 300;
    private static final short HEIGHTALERT = 200;

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
        T retour = ModelFactory.build(typeXML);
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
        T retour = ModelFactory.build(typeXML);
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
     *            Fichier à suvagarder, doit implémenter l'interface {@link model.interfaces.XML}.
     * @throws JAXBException
     */
    public boolean saveXML(XML fichier)
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

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Affichage alerte du controle des données
     */
    public void createAlert()
    {
        String texte = proprietesXML.controleDonnees();
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

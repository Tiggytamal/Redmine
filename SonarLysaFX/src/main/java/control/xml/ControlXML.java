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
    private static final short WIDTHALERT = 300;
    private static final short HEIGHTALERT = 200;

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
        T retour = ModelFactory.build(typeXML);
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
        T retour = ModelFactory.build(typeXML);
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
     *            Fichier � suvagarder, doit impl�menter l'interface {@link model.interfaces.XML}.
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

            // Contr�le que le fichier a bien �t� mis � jour
            return time < fichier.getFile().lastModified();
        }
        catch (JAXBException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible de sauvegarder le fichier de propri�t�", e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Affichage alerte du controle des donn�es
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

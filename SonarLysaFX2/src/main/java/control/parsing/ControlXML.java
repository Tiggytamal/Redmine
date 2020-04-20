package control.parsing;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.AbstractModele;
import model.ModelFactory;
import model.parsing.ProprietesPersoXML;
import model.parsing.XML;
import utilities.AbstractToStringImpl;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de gestion des paramètres de XML de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlXML extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    private static final String ERREURFICHIER = "Impossible de trouver le fichier de paramètres : ";

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Récupère les paramètres depuis le fichier externe.
     * 
     * @param typeXML
     *                Classe du fichier de paramètres à récupérer
     * @param          <T>
     *                 Classe du modèle XML.           
     * @return le fichier XML désérialisé
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractModele & XML> T recupererXML(Class<T> typeXML)
    {
        // Variables
        T retour = ModelFactory.build(typeXML);
        File file = retour.getFile();

        if (file == null)
            throw new TechnicalException(ERREURFICHIER + "Nom du fichier null");

        if (file.getName().isEmpty())
            throw new TechnicalException(ERREURFICHIER + "Nom du fichier vide");

        if (!file.exists())
            throw new TechnicalException(ERREURFICHIER + file.getPath());

        // Unmarshalling
        JAXBContext context;
        try
        {
            context = JAXBContext.newInstance(typeXML);
            retour = (T) context.createUnmarshaller().unmarshal(file);

        }
        catch (JAXBException e)
        {
            throw new TechnicalException(ERREURFICHIER + "erreur JAXB", e);
        }

        return retour;
    }

    /**
     * Récupère les paramètres depuis le fichier externe avec le nom en paramètre.
     * 
     * @param nomFichier
     *                   Nom du fichier a utiliser.
     * @return le fichier XML désérialisé
     */
    public ProprietesPersoXML recupererXMLPersoParNom(String nomFichier)
    {
        // Création du nom de fichier
        File file = new File(Statics.JARPATH + "\\\\" + nomFichier + ".xml");

        // Création d'un fichier vide s'il n'existe pas encore
        if (!file.exists())
        {
            try
            {
                // Création DocumentBuilder
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
                DocumentBuilder docBuilder = factory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("proprietesPersoXML");
                doc.appendChild(rootElement);

                // Transformation du fichier source en xml avec paramètres de sécurité
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, Statics.EMPTY);
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, Statics.EMPTY);
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            }
            catch (ParserConfigurationException | TransformerException e)
            {
                throw new TechnicalException(ERREURFICHIER + "création du fichier", e);
            }
        }

        // Unmarshalling
        JAXBContext context;
        ProprietesPersoXML retour;
        try
        {
            context = JAXBContext.newInstance(ProprietesPersoXML.class);
            retour = (ProprietesPersoXML) context.createUnmarshaller().unmarshal(file);
            retour.setFile(file);
        }
        catch (JAXBException e)
        {
            throw new TechnicalException(ERREURFICHIER + "erreur JAXB", e);
        }

        return retour;
    }

    /**
     * Sauvegarde le fichier de paramètres. Retourne vrai si le fichier a bien été mis à jour.
     * 
     * @param fichier
     *                Fichier à sauvegarder, doit implementer l'interface {@link model.parsing.XML}.
     * @return
     *         vrai si le fichier a bien été sauvegardé.
     */
    public boolean saveXML(XML fichier)
    {
        try
        {
            long time = fichier.getFile().lastModified();
            JAXBContext context = JAXBContext.newInstance(fichier.getClass());
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(fichier, fichier.getFile());

            // Contrôle que le fichier a bien été mis à jour
            return time < fichier.getFile().lastModified();
        }
        catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de sauvegarder le fichier de propriete", e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

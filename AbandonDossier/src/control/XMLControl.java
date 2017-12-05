package control;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.Main;
import model.xml.BanqueXML;
import model.xml.Parametre;
import utilities.Utilities;
import utilities.enums.Severity;
import view.MainScreen;

public class XMLControl
{
    /* ---------- ATTIBUTES ---------- */

    /** Objet contenant les param�tres du programme venant du xml. */
    private Parametre param;

    /** Param�tres du programme. */
    private String jarPath;
    
    private HashMap<String,BanqueXML> listeBanques;

    /* ---------- CONSTUCTORS ---------- */

    private XMLControl()
    {
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        param = new Parametre();
        listeBanques = new HashMap<>();
        recupParam();
    }

    private static class XMLControlHelper
    {
        private static final XMLControl INSTANCE = new XMLControl();
    }

    public static XMLControl getInstance()
    {
        return XMLControlHelper.INSTANCE;
    }

    /* ---------- METHODS ---------- */


    /**
     * R�cup�re les param�tre de connexion depuis le fichier XML s'il est pr�sent.
     * 
     * @return
     *         retourne vrai si le fichier est pr�sent et que tous les param�tres sont bons.
     * @throws FunctionalException 
     */
    private void recupParam()
    {
        File file = new File(jarPath + "\\param.xml"); // Creation objet correstondant au fichier XML

        if (file.exists())
        {
            try
            {
                // Cr�ation objet Parametre depuis le XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                param = (Parametre) jaxbUnmarshaller.unmarshal(file);
            }
            catch (JAXBException e)
            {
                // Si on a une erreur utilisation des donn�es par d�fault et remont�e d'un message d'information
                MainScreen.createAlert(Severity.SEVERITY_INFO, e, "Impossible de d�chiffrer le fichier de param�trage, utilisation des donn�es par default");
                createXML();
            }
        }
        else
            {
                // Si le fichier n'existe pas, cr�ation d'un message d'information et utilisation des donn�es par d�fault
                MainScreen.createAlert(Severity.SEVERITY_INFO, null, "Fichier de param�trage in�xistant, utilisant donn�es par default");
                createXML();
            }

        // Affectation des param�tres du programme
        for (BanqueXML banque : param.getListBanqueXML())
        {
            listeBanques.put(banque.getNom(), banque);
        }
    }
    
    /**
     * M�thode de cr�ation de param�tres par d�fault en cas de probl�me
     */
    private void createXML()
    {
        // Cr�ation des donn�es par d�fault des banques
        Parametre parametre = new Parametre();
        List<BanqueXML> liste = new ArrayList<>();
        liste.add(new BanqueXML("BPBFC", "2", "008", "31"));
        liste.add(new BanqueXML("BPAURA", "3", "068", "31"));
        liste.add(new BanqueXML("BPRI", "C", "002", "31"));
        liste.add(new BanqueXML("BQSAV", "S", "552", "31"));
        liste.add(new BanqueXML("CASDN", "1", "013", "31"));
        liste.add(new BanqueXML("BPVF", "8", "087", "31"));
        liste.add(new BanqueXML("BPPN", "J", "035", "31"));
        liste.add(new BanqueXML("BPS", "P", "066", "31"));
        liste.add(new BanqueXML("BPO", "B", "067", "31"));
        liste.add(new BanqueXML("CMMBN", "W", "534", "31"));
        liste.add(new BanqueXML("BPATL", "D", "038", "71"));
        liste.add(new BanqueXML("BPMED", "G", "046", "31"));
        liste.add(new BanqueXML("CMMAT", "Z", "536", "31"));
        liste.add(new BanqueXML("BPACA", "M", "009", "31"));
        liste.add(new BanqueXML("BPALC", "N", "047", "31"));
        liste.add(new BanqueXML("BPOC", "Q", "078", "31"));
        liste.add(new BanqueXML("CMSOU", "T", "537", "31"));        
        parametre.setListBanqueXML(liste);
        parametre.setNomFichier("TN5B-TN5F");
        parametre.setUrl("C:\\Users\\Tiggy Tamal\\Downloads\\Mes Docs");

        // Assignation des donn�es au programme
        param = parametre;
        saveXML();
    }
    
    public void saveXML()
    {
        try
        {
            File file = new File(jarPath + "\\param.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(param, file);

        } catch (JAXBException e)
        {
            MainScreen.createAlert(Severity.SEVERITY_ERROR, e, "Impossible de cr�er le fichier de param�tre par default");
        }
    }
    
    

    /* ---------- ACCESS ---------- */

    /**
     * @return the jarPath
     */
    public String getJarPath()
    {
        return jarPath;
    }   

    /**
     * @return the param
     */
    public Parametre getParam()
    {
        return param;
    }

    /**
     * @return the listeBanques
     */
    public HashMap<String, BanqueXML> getListeBanques()
    {
        return listeBanques;
    }
}
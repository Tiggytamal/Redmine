package control;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.Main;
import model.xml.BanquesXML;
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
    private String jarPath, url;
    
    private HashMap<String,String> listeBanques;

    /* ---------- CONSTUCTORS ---------- */

    private XMLControl()
    {
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        param = new Parametre();
        listeBanques = new HashMap<>();
        recupParam();
        saveParametre();
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
    public void recupParam()
    {
        File file = new File(jarPath + "\\param.xml"); // Creation objet correstondant au fichier XML
        boolean paramOK = false; // boolean pour v�rifier si le fichier XML existe

        if (file.exists())
        {
            try
            {
                // Cr�ation objet Parametre depuis le XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                param = (Parametre) jaxbUnmarshaller.unmarshal(file);
                paramOK = true;
            }
            catch (JAXBException e)
            {
                MainScreen.createAlert(Severity.SEVERITY_INFO, null, null);
            }
        }
        else
            System.out.println("Le fichier XML n'existe pas");

        // On v�rifie que le XML contient bien tous les param�tres
        if (paramOK == true && (param.getUrl() == null))
            System.out.println("Certaines valeurs du XML sont nulles");
        else if (paramOK == true)
        {
            // Affectation des param�tres du programme
            System.out.println("utilisation des valeurs du xml");
            url = param.getUrl();
            for (BanquesXML banque : param.getListBanques())
            {
                listeBanques.put(banque.getNom(), banque.getCoad());
            }
            
        }
    }
    
    public void saveParametre()
    {
        try
        {
            Parametre param2 = new Parametre();
            param2.setUrl("aaa");
            BanquesXML b = new BanquesXML();
            b.setCoad("C");
            b.setNom("nom");
            param2.setListBanques(new ArrayList<>());
            param2.getListBanques().add(b);
            File file = new File(jarPath + "param.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(param2, file);

        } catch (JAXBException e)
        {
            e.printStackTrace();
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
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @return the listeBanques
     */
    public HashMap<String, String> getListeBanques()
    {
        return listeBanques;
    }
}

package control;

import java.io.File;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.Main;
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
    private String user, pass, driver, jarPath, url;

    /** COntroleur */
    private ListControl listControl;

    /* ---------- CONSTUCTORS ---------- */

    private XMLControl()
    {
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        System.out.println(jarPath);
        listControl = ListControl.getInstance();
        listControl.setPath(jarPath);
        param = new Parametre();
    }

    private static class XMLControlHelper
    {
    	private XMLControlHelper() {}
        private static final XMLControl INSTANCE = new XMLControl();
    }

    public static XMLControl getInstance()
    {
        return XMLControlHelper.INSTANCE;
    }

    /* ---------- METHODS ---------- */

    /**
     * Permet de cr�er le DAO pour les incidents � partir d'un fichier de param�trage.
     * 
     * @return
     *         Une instance de <code>DaoIncident</code>
     * @throws FunctionalException 
     */
    public void miseAJourParam()
    {
        recupParam();
        recupDefault();
        saveParamJPA();
    }

    /**
     * R�cup�re les param�tre de connexion depuis le fichier XML s'il est pr�sent.
     * 
     * @return
     *         retourne vrai si le fichier est pr�sent et que tous les param�tres sont bons.
     * @throws FunctionalException 
     */
    private void recupParam()
    {
    	// Creation objet correstondant au fichier XML
        File file = new File(jarPath + "\\param.xml"); 
        // boolean pour v�rifier si le fichier XML existe
        boolean paramOK = false; 

        if (file.exists())
        {
            try
            {
                // Cr�ation objet Parametre depuis le XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                param = (Parametre) jaxbUnmarshaller.unmarshal(file);
                listControl.setParam(param);
                paramOK = true;
            }
            catch (JAXBException e)
            {
                MainScreen.createAlert(Severity.SEVERITY_INFO, null, null);
            }
        }
        else
        {
        	System.out.println("Le fichier XML n'existe pas");
        }

        // On v�rifie que le XML contient bien tous les param�tres
        if (paramOK == true && (param.getDriver() == null || param.getPass() == null || param.getUrl() == null || param.getUser() == null))
        {
        	System.out.println("Certaines valeurs du XML sont nulles");
        }
        else if (paramOK)
        {
            // Affectation des param�tres du programme
            System.out.println("utilisation des valeurs du xml");
            driver = param.getDriver();
            user = param.getUser();
            pass = param.getPass();
            url = param.getUrl();
        }
    }

    /**
     * Initialise les param�tres par default.
     */
    private void recupDefault()
    {
    	// Creation map des propri�t�s
        HashMap<String, String> map = new HashMap<>(); 

        // On ajoute le param�tre dans la map s'il a �t� renseign�
        if (pass != null)
        {
        	map.put("javax.persistence.jdbc.password", pass);
        }
        if (driver != null)
        {
        	map.put("javax.persistence.jdbc.driver", driver);
        }
        if (url != null)
        {
        	map.put("javax.persistence.jdbc.url", url);
        }
        if (user != null)
        {
        	map.put("javax.persistence.jdbc.user", user);
        }

        // Creation de la connection et on r�cup�re les param�tres
        listControl.createFactory(map);
        EntityManager manager = listControl.getFactory().getManager().getEm();
        driver = (String) manager.getProperties().get("javax.persistence.jdbc.driver");
        url = (String) manager.getProperties().get("javax.persistence.jdbc.url");
        user = (String) manager.getProperties().get("javax.persistence.jdbc.user");
        pass = (String) manager.getProperties().get("javax.persistence.jdbc.password");
    }

    /**
     * Sauvegarde les param�tres dans un XML.
     */
    private void saveParamJPA()
    {
        param.setDriver(driver);
        param.setPass(pass);
        param.setUrl(url);
        param.setUser(user);
        saveParametre();
    }

    public void saveParametre()
    {
        try
        {
            File file = new File(jarPath + "\\param.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(param, file);

        }
        catch (JAXBException e)
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
}

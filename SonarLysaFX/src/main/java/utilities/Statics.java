package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.xml.ControlXML;
import javafx.stage.FileChooser;
import model.FichiersXML;
import model.Info;
import model.ModelFactory;
import model.ProprietesXML;

/**
 * Classe regroupant toutes les constantes statiques
 * 
 * @author ETP8137 - Gr�goire Mathon
 */
public class Statics
{
    // Supression instanciation de la classe
    private Statics() 
    {
        throw new AssertionError();
    }

    /* ----- Param�tres fixes ----- */
    /** jarPath */
    public static final String JARPATH = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
    /** filter pour fichiers Excel */
    public static final FileChooser.ExtensionFilter FILTEREXCEL = new FileChooser.ExtensionFilter("Fichiers Excel (*.xls)", "*.xls", "*.xlsx", "*.xlsm");
    /** fin du lien g�n�rique pur les anomalies RTC */
    public static final String FINLIENSANO = "#action=com.ibm.team.workitem.viewWorkItem&id=";
    /** Hauteur d'une ligne */
    public static final int ROW_HEIGHT = 24;
    /** Largeur d'un caract�re */
    public static final double CARAC_WIDTH = 7;
    
    public static final String RESOURCESTEST = "/";
    
    /* ----- loggers ----- */
    
    /** logger g�n�ral */
	public static final Logger logger = LogManager.getLogger("complet.log");
    /** logger composants sans applications */
    public static final Logger logSansApp = LogManager.getLogger("sansapp-log");
    /** logger composants avec application INCONNUE*/
    public static final Logger loginconnue = LogManager.getLogger("inconnue-log");
    /** logger applications non list�e dans le r�f�rentiel */
    public static final Logger lognonlistee = LogManager.getLogger("nonlistee-log");
    /** logger plantages de l'application */
    public static final Logger logPlantage = LogManager.getLogger("plantage-log"); 
    
    /* ----- String statiques ----- */
    
    /** Nom de l'application */
    public static final String NOMAPPLI = "SonarLyza";
    /** Valeur pour le s�parateur de ligne ind�pendant du syst�me */
    public static final String NL = System.getProperty("line.separator");
    /** Indiqe une anomalie avec un probl�me de s�curit� */
    public static final String SECURITEKO = "X";
    /** Mois de janvier */
    public static final String JANVIER = "Janvier";
    /** Mois de janvier */
    public static final String FEVRIER = "F�vrier";
    /** Mois de janvier */
    public static final String MARS = "Mars";
    /** Mois de janvier */
    public static final String AVRIL = "Avril";
    /** Mois de janvier */
    public static final String MAI = "Mai";
    /** Mois de janvier */
    public static final String JUIN = "Juin";
    /** Mois de janvier */
    public static final String JUILLET = "Juillet";
    /** Mois de janvier */
    public static final String AOUT = "Aout";
    /** Mois de janvier */
    public static final String SEPTEMBRE = "Septembre";
    /** Mois de janvier */
    public static final String OCTOBRE = "Octobre";
    /** Mois de janvier */
    public static final String NOVEMBRE = "Novembre";
    /** Mois de janvier */
    public static final String DECEMBRE = "Decembre";
    /** espace */
    public static final String SPACE = " ";
    /** " : " */
    public static final String DEUXPOINTS = " : ";   
    /** apllication inconnue dans Sonar */
    public static final String INCONNUE = "INCONNUE";
    /** donn�e inconnue */
    public static final String INCONNU = "INCONNU";

    /* ----- Objets statiques ----- */
    
    /** Wrapper des informations g�n�rales de fonctionnement de l'application*/
    public static final Info info = ModelFactory.getModel(Info.class);
    /** Controleur XML */
    private static final ControlXML controlXML = new ControlXML();
    /** Sauvegarde des fichiers Excel de param�tre */
    public static final FichiersXML fichiersXML = controlXML.recupererXML(FichiersXML.class);
    /** Sauvegarde des fichiers Excel de param�tre */
    public static final ProprietesXML proprietesXML = controlXML.recupererXML(ProprietesXML.class);
}
package utilities;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.ControlXML;
import javafx.stage.FileChooser;
import model.FichiersXML;
import model.Info;
import model.ProprietesXML;

/**
 * Classe regroupant toutes les constantes statiques
 * 
 * @author ETP8137 - Grégoire Mathon
 */
public abstract class Statics
{
    private Statics() {}

    /** Wrapper des informations générales de fonctionnement de l'application*/
    public static final Info info = new Info();
    /** jarPath */
    public static final String JARPATH = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
    /** logger général */
	public static final Logger logger = LogManager.getLogger("complet.log");
    /** logger composants sans applications */
    public static final Logger logSansApp = LogManager.getLogger("sansapp-log");
    /** logger composants avec application INCONNUE*/
    public static final Logger loginconnue = LogManager.getLogger("inconnue-log");
    /** logger applications non listée dans le référentiel */
    public static final Logger lognonlistee = LogManager.getLogger("nonlistee-log");
    /** Nom de l'application */
    public static final String NOMAPPLI = "SonarLyza";
    /** Valeur pour le séparateur de ligne indépendant du système */
    public static final String NL = System.getProperty("line.separator");
    /** Date du jour */
    public static final LocalDate TODAY = LocalDate.now();
    /** Mois de janvier */
    public static final String JANVIER = "Janvier";
    /** Mois de janvier */
    public static final String FEVRIER = "Février";
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
    /** apllication inconnue dans Sonar */
    public static final String INCONNUE = "INCONNUE";
    /** filter pour fichiers Excel */
    public static final FileChooser.ExtensionFilter FILTEREXCEL = new FileChooser.ExtensionFilter("Fichiers Excel (*.xls)", "*.xls", "*.xlsx", "*.xlsm");
    /** Controleur XML */
    private static final ControlXML controlXML = new ControlXML();
    /** Sauvegarde des fichiers Excel de paramètre */
    public static final FichiersXML fichiersXML = (FichiersXML) controlXML.recupererXML(FichiersXML.class);
    /** Sauvegarde des fichiers Excel de paramètre */
    public static final ProprietesXML proprietesXML = (ProprietesXML) controlXML.recupererXML(ProprietesXML.class);
}
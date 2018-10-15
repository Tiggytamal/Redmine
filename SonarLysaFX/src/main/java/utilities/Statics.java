package utilities;

import application.Main;
import control.xml.ControlXML;
import javafx.stage.FileChooser;
import model.Info;
import model.ModelFactory;
import model.ProprietesXML;

/**
 * Classe regroupant toutes les constantes statiques
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public final class Statics
{   
    /*---------- ATTRIBUTS ----------*/
    
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
    /** Taille du substring pour les lots */
    public static final short SBTRINGLOT = 4;
    /** Divisuer millisecondes */
    public static final int MILLITOSECOND = 1000;
    
    /** Adresse g�n�rale pour les resources */
    public static final String ROOT = "/";
    public static final String RESSTEST = "src/test/resources/";
    
    /* ----- String statiques ----- */
    
    /** Nom de l'application */
    public static final String NOMAPPLI = "SonarLysa";
    /** Valeur pour le s�parateur de ligne ind�pendant du syst�me */
    public static final String NL = System.getProperty("line.separator");
    /** Indiqe une anomalie avec un probl�me de s�curit� */
    public static final String X = "X";
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
    /** String vide */
    public static final String EMPTY = "";
    /** " : " */
    public static final String DEUXPOINTS = " : ";   
    /** apllication inconnue dans Sonar */
    public static final String INCONNUE = "INCONNUE";
    /** donn�e inconnue */
    public static final String INCONNU = "INCONNU";
    /** Nom fichier css */
    public static final String CSS = "application.css";
    /** liens texte vers anomalie */
    public static final String LIENANO = " - Anomalie : ";
    /** liens texte vers application */    
    public static final String LIENAPP = " - Application : ";
    /** Adresse g�n�rique de d�serialisation */
    public static final String ADRESSEDESER = "d:\\";
    /** Tiret */
    public static final String TIRET = "- ";
    /** Anomalie CLose */
    public static final String ANOCLOSE = "Close";
    /** commentaire, balise @Ignore des Junit */
    public static final String TESTMANUEL = "Test manuel - ne pas utiliser en automatique";
    
    /* ----- Objets statiques ----- */
    
    /** Wrapper des informations g�n�rales de fonctionnement de l'application*/
    public static final Info info = ModelFactory.getModel(Info.class);
    /** Controleur XML */
    private static final ControlXML controlXML = new ControlXML();
    /** Sauvegarde des fichiers Excel de param�tre */
    public static final ProprietesXML proprietesXML = controlXML.recupererXML(ProprietesXML.class);
    
    /*---------- CONSTRUCTEURS ----------*/
    
    // Supression instanciation de la classe
    private Statics() 
    {
        throw new AssertionError();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

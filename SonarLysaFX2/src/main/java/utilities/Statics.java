package utilities;

import java.time.LocalDate;
import java.util.regex.Pattern;

import application.Main;
import control.parsing.ControlXML;
import dao.DaoFactory;
import javafx.stage.FileChooser;
import model.Info;
import model.ModelFactory;
import model.bdd.ChefService;
import model.bdd.Edition;
import model.bdd.ProjetClarity;
import model.bdd.Utilisateur;
import model.parsing.ProprietesPersoXML;
import model.parsing.ProprietesXML;

/**
 * Classe regroupant toutes les constantes statiques
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public final class Statics
{
    /*---------- ATTRIBUTS ----------*/

    /* ----- Paramètres fixes ----- */
    /** jarPath */
    public static final String JARPATH = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
    /** filter pour fichiers Excel */
    public static final FileChooser.ExtensionFilter FILTEREXCEL = new FileChooser.ExtensionFilter("Fichiers Excel (*.xlsx)", "*.xlsx", "*.xlsm", "*.xls");
    /** fin du lien générique pur les anomalies RTC */
    public static final String FINLIENSRTC = "#action=com.ibm.team.workitem.viewWorkItem&id=";
    /** Hauteur d'une ligne */
    public static final int ROW_HEIGHT = 24;
    /** Largeur d'un caractere */
    public static final double CARAC_WIDTH = 7;
    /** Multiplicateur création Iterable */
    public static final double RATIOLOAD = 1.25d;
    /** Taille du substring pour les lots */
    public static final short SBTRINGLOT = 4;
    /** Diviseur millisecondes */
    public static final int MILLITOSECOND = 1000;
    /** 1 seconde en millisecondes */
    public static final int SECOND = 1000;
    /** zéro sous forme de String */
    public static final String ZERO = "0";
    /** Date inconnue initialisee au 1er Janvier 2000 */
    public static final LocalDate DATEINCONNUE = LocalDate.of(2000, 1, 1);
    /** Date inconnue au 1er Janvier 2099 */
    public static final LocalDate DATEINCO2099 = LocalDate.of(2099, 1, 1);
    /** Aujourd'hui */
    public static final LocalDate TODAY = LocalDate.now();
    /** Nombre de millisecondes dans une journée */
    public static final Double MILLIJOUR = 86400000d;
    /** Nombre de jour dans une semaine */
    public static final int JOURSEMAINE = 7;
    /** NOmbre de jours par mois en moyenne */
    public static final float JOURSMOIS = 30.41f;
    /** Adresse générale pour les resources */
    public static final String ROOT = "/";
    /** Adresse générale pour les resources de test */
    public static final String RESSTEST = "src/test/resources/";

    /* ----- String statiques ----- */

    /** Nom de l'application */
    public static final String NOMAPPLI = "SonarLysaFX 2";
    /** Valeur pour le separateur de ligne independant du systeme */
    public static final String NL = System.getProperty("line.separator");
    /** Indiqe une anomalie avec un problème de securite */
    public static final String X = "X";
    /** Mois de janvier */
    public static final String JANVIER = "Janvier";
    /** Mois de janvier */
    public static final String FEVRIER = "Fevrier";
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
    public static final char SPACE = ' ';
    /** String vide */
    public static final String EMPTY = "";
    /** " : " */
    public static final String DEUXPOINTS = " : ";
    /** apllication inconnue dans Sonar */
    public static final String INCONNUE = "INCONNUE";
    /** donnée inconnue */
    public static final String INCONNU = "INCONNU";
    /** Nom fichier css */
    public static final String CSS = "application.css";
    /** liens texte vers anomalie */
    public static final String LIENANO = " - Anomalie : ";
    /** liens texte vers application */
    public static final String LIENAPP = " - Application : ";
    /** Adresse générique de deserialisation */
    public static final String ADRESSEDESER = "d:\\";
    /** Tiret avec espace final */
    public static final String TIRET = "- ";
    /** Tiret avec deux espaces */
    public static final String TIRET2 = " - ";
    /** Anomalie CLose */
    public static final String ANOCLOSE = "Close";
    /** Anomalie CLose Produit */
    public static final String ANOCLOSEPRODUIT = "9.Validée";
    /** Edition inconnue */
    public static final String EDINCONNUE = "00.00.00.00";

    /* ----- Patterns statiques ----- */

    /** Pattern représentant un espace */
    public static final Pattern PATTERNSPACE = Pattern.compile("\\s");
    /** Pattern représentant un nombre */
    public static final Pattern PATTERNNBRE = Pattern.compile("\\d+");

    /* ----- Objets statiques ----- */

    /** Wrapper des informations générales de fonctionnement de l'application */
    public static final Info info = ModelFactory.build(Info.class);
    /** Controleur XML */
    private static final ControlXML controlXML = new ControlXML();
    /** Sauvegarde des fichiers Excel de paramètre */
    public static final ProprietesXML proprietesXML = controlXML.recupererXML(ProprietesXML.class);
    /** Sauvegarde des fichiers Excel des paramètres personnalisés */
    public static final ProprietesPersoXML propPersoXML = ModelFactory.build(ProprietesPersoXML.class);
    /** Chef de service générique d'un service inconnu */
    public static final ChefService CHEFSERVINCONNU = DaoFactory.getMySQLDao(ChefService.class).recupEltParIndex(INCONNU);
    /** Edition inconnue */
    public static final Edition EDITIONINCONNUE = DaoFactory.getMySQLDao(Edition.class).recupEltParIndex(EDINCONNUE);
    /** Projet Clarity inconnu */
    public static final ProjetClarity CLARITYINCONNU = DaoFactory.getMySQLDao(ProjetClarity.class).recupEltParIndex(INCONNU);
    /** Utilisateur inconnu */
    public static final Utilisateur USERINCONNU = DaoFactory.getMySQLDao(Utilisateur.class).recupEltParIndex(INCONNU);

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

package fxml;

import static application.MainScreen.ROOT;
import static utilities.Statics.info;

import java.io.IOException;
import java.util.Optional;

import application.MainScreen;
import control.parsing.ControlXML;
import control.rest.SonarAPI;
import control.rtc.ControlRTC;
import control.statistique.ControlStatistique;
import control.task.AbstractTask;
import control.task.LaunchTask;
import control.task.maj.AssignerAnoTask;
import control.task.maj.InitComposantsSonarTask;
import control.watcher.ControlWatch;
import dao.DaoComposantBase;
import dao.DaoFactory;
import fxml.bdd.GestionBDD;
import fxml.dialog.CalcDialog;
import fxml.dialog.ConnexionDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import model.bdd.ComposantBase;
import model.enums.EtatFichierPic;
import model.enums.OptionVisibilite;
import model.enums.ParamBool;
import model.enums.Severity;
import model.parsing.ProprietesPersoXML;
import utilities.AbstractToStringImpl;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.UpdateNbreFichiers;

/**
 * Gestion de l'affichage du menu de l'application.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class Menu extends AbstractToStringImpl implements SubView
{
    /* ---------- ATTIBUTS ---------- */

    // Constantes statiques
    private static final short WIDTHAIDE = 640;
    private static final short HEIGHTAIDE = 480;
    private static final String OUTILS = "Outils/";
    private static final String CREATIONS = "Créations/";
    private static final String BDD = "BDD/";
    private static final String STATISTIQUES = "Statistiques/";
    private static final int SEUILVERT = 18_000_000;
    private static final int SEUILJAUNE = 19_000_000;
    private static final int SEUILROUGE = 19_500_000;

    /** Propriété permettant de gérer l'affichage du nombre de lignes de code sans avoir d'instance du controleur */
    private static final SimpleStringProperty ldcTextProperty = new SimpleStringProperty(Statics.EMPTY);

    /** Propriété permettant de gérer la couleur de l'affichage du nombre de lignes de code sans avoir d'instance du controleur */
    private static final SimpleStringProperty ldcStyleProperty = new SimpleStringProperty(Statics.EMPTY);

    /** Propriété permettant de gérer les l'affichage des fichiers à traiter sans avoir d'instance du controleur */
    private static final SimpleStringProperty nbreFichiersTextProperty = new SimpleStringProperty(Statics.EMPTY);

    /** Propriété permettant de gérer la couleur de l'affichage des fichiers à traiter sans avoir d'instance du controleur */
    private static final SimpleStringProperty nbreFichiersStyleProperty = new SimpleStringProperty(Statics.EMPTY);

    // Elements du menu
    @FXML
    private MenuItem mensuel;
    @FXML
    private MenuItem options;
    @FXML
    private MenuItem rtc;
    @FXML
    private MenuItem issue;
    @FXML
    private MenuItem autres;
    @FXML
    private MenuItem extraction;
    @FXML
    private MenuItem maintenance;
    @FXML
    private MenuItem graphique;
    @FXML
    private MenuItem pourcentage;
    @FXML
    private MenuItem calculatrice;
    @FXML
    private MenuItem planification;
    @FXML
    private MenuItem aide;
    @FXML
    private MenuItem fichierPic;
    @FXML
    private MenuItem majCompos;
    @FXML
    private MenuItem defautsQualite;
    @FXML
    private MenuItem composants;
    @FXML
    private MenuItem composPlantes;
    @FXML
    private MenuItem anos;
    @FXML
    private MenuItem users;
    @FXML
    private Label ldc;
    @FXML
    private Button connexion;
    @FXML
    private Button deConnexion;
    @FXML
    private HBox box;
    @FXML
    private Label nbreFichiers;

    /** Affichage du HTML d'aide de l'application */
    private Alert aidePanel;

    private ControlRTC controlRTC;

    private static final DaoComposantBase daoCompos = DaoFactory.getMySQLDao(ComposantBase.class);

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        // Suppression bouton connecté
        box.getChildren().remove(deConnexion);

        // Mise en place texte et style du nombre de lignes de code
        ldc.textProperty().bind(ldcTextProperty);
        ldc.styleProperty().bind(ldcStyleProperty);
        updateNbreLignes();

        // Mise en place texte et effet pour le nombre de fichiers à traiter
        nbreFichiers.textProperty().bind(nbreFichiersTextProperty);
        nbreFichiers.styleProperty().bind(nbreFichiersStyleProperty);
        updateFichiersActif(EtatFichierPic.INACTIF);
    }

    /* ---------- METHODES PUBLIQUES ---------- */

    /**
     * Permet d'afficher les nouveaux écrans ou de lancer les traitements après confirmation.
     * 
     * @param event
     *              Evenement déclenché à la selection dans le menu.
     */
    @Override
    public void afficher(ActionEvent event) throws IOException
    {
        String id;
        String texte;
        Object source = event.getSource();

        if (source instanceof MenuItem)
        {
            id = ((MenuItem) source).getId();

            // Récupère le nom de la fenêtre et enlève les "_" utilisés pour les raccourcis clavier.
            texte = ((MenuItem) source).getText().replace("_", Statics.EMPTY);
        }
        else
            throw new TechnicalException("Source non gérée : " + source, null);

        if (!info.controle() && !"options".equals(id))
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, Merci de vous connecter");

        switch (id)
        {
            // Chargement des écrans
            case "mensuel":
                load("/fxml/TEP.fxml");
                majTitreCREATIONS(texte);
                break;

            case "autres":
                load("/fxml/AutresPortfolios.fxml");
                majTitreCREATIONS(texte);
                break;

            case "maintenance":
                load("/fxml/Maintenance.fxml");
                majTitreCREATIONS(texte);
                break;

            case "extraction":
                load("/fxml/Extractions.fxml");
                majTitreCREATIONS(texte);
                break;

            case "graphique":
                load("/fxml/Statistiques.fxml");
                majTitreSTATS(texte);
                break;

            case "pourcentage":
                load("/fxml/Pourcentages.fxml");
                majTitreSTATS(texte);
                break;

            case "fichierPic":
                load("/fxml/FichierPic.fxml");
                majTitreOUTILS(texte);
                break;

            case "rtc":
                load("/fxml/MajLotsRTC.fxml");
                majTitreOUTILS(texte);
                break;

            case "options":
                load("/fxml/Options.fxml");
                majTitreOUTILS(texte);
                break;
                
            case "planification":
                load("/fxml/Planification.fxml");
                majTitreOUTILS(texte);
                break;

            case "defautsQualite":
                loadDatabase("/fxml/bdd/DefaultQualiteBDD.fxml");
                majTitreBDD(texte);
                break;

            case "composants":
                loadDatabase("/fxml/bdd/ComposantBDD.fxml");
                majTitreBDD(texte);
                break;

            case "anos":
                loadDatabase("/fxml/bdd/AnomalieRTCBDD.fxml");
                majTitreBDD(texte);
                break;

            case "composPlantes":
                loadDatabase("/fxml/bdd/ComposantPlanteBDD.fxml");
                majTitreBDD(texte);
                break;

            case "users":
                loadDatabase("/fxml/bdd/UtilisateurBDD.fxml");
                majTitreBDD(texte);
                break;

            // Demande confirmations pour traitements
            case "majCompos":
                alertConfirmation(new InitComposantsSonarTask(), "Cela lancera la mise à jour de tous les composants.");
                break;

            case "issue":
                alertConfirmation(new AssignerAnoTask(), "Cela lancera l'assignation des anomalies.");
                break;

            default:
                throw new TechnicalException("Méthode control.view.MenuViewControl.afficher - MenuItem pas géré : " + id, null);
        }
    }

    /**
     * Mise à jour de l'affichage du nombre de lignes du serveur SonarQube
     */
    public static void updateNbreLignes()
    {
        long nbreLignes = daoCompos.recupNombreLigne();
        ldcTextProperty.set(String.valueOf(nbreLignes));
        if (nbreLignes >= SEUILROUGE)
            ldcStyleProperty.set("-fx-text-fill: rgb(255,0,0)");
        else if (nbreLignes >= SEUILJAUNE)
            ldcStyleProperty.set("-fx-text-fill: rgb(255,255,0)");
        else if (nbreLignes >= SEUILVERT)
            ldcStyleProperty.set("-fx-text-fill: rgb(0,255,0)");
        else
            ldcStyleProperty.set("-fx-text-fill: rgb(255,255,255)");

    }

    /**
     * Mise à jour de l'effet pour indiquer si le traitement des fichiers du quai est actif ou non.<br>
     * Texte en trouge : inactif.<br>
     * Texte en vert : actif.
     * 
     * @param etat
     *             Etat de traitement des fichiers PIC.
     */
    public static void updateFichiersActif(EtatFichierPic etat)
    {
        if (etat == EtatFichierPic.ACTIF)
            nbreFichiersStyleProperty.set("-fx-text-fill: rgb(0,255,0)");
        else
            nbreFichiersStyleProperty.set("-fx-text-fill: rgb(255,0,0)");
    }

    /* ---------- METHODES PRIVEES ---------- */

    /**
     * Ouvre la fénêtre de connexion
     */
    @FXML
    private void openPopup()
    {
        Optional<Pair<String, String>> result = new ConnexionDialog().showAndWait();
        result.ifPresent(pair -> testMdP(pair.getKey(), pair.getValue()));
    }

    /**
     * Traitement de déconnexion à l'application
     * 
     */
    @FXML
    private void deco()
    {
        // Désactivation des boutons
        mensuel.setDisable(true);
        issue.setDisable(true);
        fichierPic.setDisable(true);
        majCompos.setDisable(true);
        defautsQualite.setDisable(true);
        composants.setDisable(true);
        composPlantes.setDisable(true);
        planification.setDisable(true);
        pourcentage.setDisable(true);
        graphique.setDisable(true);
        calculatrice.setDisable(true);
        anos.setDisable(true);
        users.setDisable(true);
        rtc.setDisable(true);
        extraction.setDisable(true);
        autres.setDisable(true);

        // Désactivation du menu personnel des options
        Options.disablePerso.set(true);

        // Suppression des mots de passe et pseudo
        info.setPseudo(Statics.EMPTY);
        info.setMotDePasse(Statics.EMPTY);

        // Arrêt traitemetn des fichiers PIC
        ControlWatch.INSTANCE.stopBoucle();
        Menu.updateFichiersActif(EtatFichierPic.INACTIF);

        // Suppression du raccourci pour lancer le traitement des fichiers de la Pic
        box.getScene().getAccelerators().remove(ControlWatch.INSTANCE.getRaccourciFichierPic());

        // Remise à zéro de l'affichage
        box.getChildren().remove(deConnexion);
        box.getChildren().add(connexion);
        ROOT.setCenter(null);
        MainScreen.majTitre(Statics.NOMAPPLI);
    }

    /**
     * Affiche le menu d'aide
     */
    @FXML
    private void aide()
    {
        // Création de la fenêtre
        aidePanel = new Alert(AlertType.NONE);
        aidePanel.initStyle(StageStyle.UTILITY);
        aidePanel.getDialogPane().getStylesheets().add(Statics.CSS);
        aidePanel.setTitle("Aide");
        aidePanel.setHeaderText(null);
        aidePanel.setContentText(null);

        // Affichage de la page HTML 5
        WebView webView = new WebView();
        webView.getEngine().load(getClass().getResource("/aide/menu.html").toString());
        webView.setPrefSize(WIDTHAIDE, HEIGHTAIDE);
        aidePanel.setResizable(true);
        aidePanel.getDialogPane().setContent(webView);
        aidePanel.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        aidePanel.show();
    }

    /**
     * Ouvre la calculatrice des statistiques.
     */
    @FXML
    private void calculer()
    {
        new CalcDialog().show();
    }

    /**
     * Teste la connexion au serveur Sonar et au serveur RTC.
     * 
     * @param pseudo
     *               Pseudo de l'utilisateur.
     * @param mdp
     *               Mot de passe de l'utilisateur.
     */
    private void testMdP(String pseudo, String mdp)
    {
        controlRTC = ControlRTC.build();

        // Sauvegarde des informations de connexion
        info.setPseudo(pseudo);
        info.setMotDePasse(mdp);

        // Contrôle connexion RTC et SonarQube
        if (!controlRTC.connexion() || !SonarAPI.build().connexionUtilisateur())
            throw new FunctionalException(Severity.INFO, "Utilisateur incorrect");

        connexion();
    }

    /**
     * Traitements necessaires lors de la connexion d'un utilisateur.
     */
    private void connexion()
    {
        recupFichierConfigPerso();

        // Lancement automatique du traitement des fichiers Pic selon paramètrage
        if (Statics.proprietesXML.getMapParamsBool().get(ParamBool.FICHIERPICAUTO))
            ControlWatch.INSTANCE.lancementBoucle();

        // Activation des boutons
        mensuel.setDisable(false);
        fichierPic.setDisable(false);
        autres.setDisable(false);
        majCompos.setDisable(false);
        issue.setDisable(false);
        pourcentage.setDisable(false);
        graphique.setDisable(false);
        calculatrice.setDisable(false);
        defautsQualite.setDisable(false);
        composPlantes.setDisable(false);
        planification.setDisable(false);
        composants.setDisable(false);
        extraction.setDisable(false);
        maintenance.setDisable(false);
        defautsQualite.setDisable(false);
        composants.setDisable(false);
        anos.setDisable(false);
        users.setDisable(false);
        rtc.setDisable(false);

        // Changement du bouton connexion
        box.getChildren().remove(connexion);
        box.getChildren().add(deConnexion);
        info.setNom(controlRTC.recupNomContributorConnecte());
        deConnexion.setText(info.getNom());

        // Ajout du raccourci pour démarrer et arréter le traitement des fichiers PIC
        box.getScene().getAccelerators().put(ControlWatch.INSTANCE.getRaccourciFichierPic(), ControlWatch.INSTANCE.getSwitchBoucle());

        // Mise à jour de l'affichage du nombre de fichiers toutes les secondes.
        new Thread(new UpdateNbreFichiers(nbreFichiersTextProperty, fichierPic)).start();

        // Activation du menu personnel des options
        Options.disablePerso.set(false);

        majStatsDemarrage();
    }

    /**
     * Récupération du fichier de configuration personnel de l'utilisateur connecté.
     */
    private void recupFichierConfigPerso()
    {
        // Récupération des informations depuis le fichier XML
        ProprietesPersoXML temp = new ControlXML().recupererXMLPersoParNom(Statics.info.getUser().getNom());

        // Copie dans l'objet statique de l'application
        Statics.propPersoXML.copie(temp);
    }

    /**
     * Lance les mise à jour journalières des statistiques.
     */
    private void majStatsDemarrage()
    {
        ControlStatistique controlStat = new ControlStatistique();

        controlStat.majStatsAnosEnCours();
        controlStat.majStatsComposKO();
        controlStat.majStatsComposSansDefaut();
        controlStat.majStatsLdcTU();
    }

    /**
     * Chargement de la nouvelle page en utilisant la ressource en paramètre.
     * 
     * @param ressource
     *                  Adresse du fichier fxml de la ressource.
     * @throws IOException
     *                     Exception lancée lors d'un erreur lors du traitement du fichier fxml.
     */
    private void load(String ressource) throws IOException
    {
        // Chargement de la page et ajout au borderPane
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ressource));
        Node pane = loader.load();
        ROOT.setCenter(pane);

        if (loader.getController() instanceof ImplShortCut)
            ((ImplShortCut) loader.getController()).implShortCut();
    }

    /**
     * Chargement d'une nouvelle page pour afficher les données de la base de données.
     * 
     * @param fxml
     *             Adresse du fichier fxml de la ressource.
     * @throws IOException
     *                     Exception lancée lors d'un erreur lors du traitement du fichier fxml.
     */
    private void loadDatabase(String fxml) throws IOException
    {
        // Chargement de la page générale des base de données
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bdd/GestionBDD.fxml"));
        Node bddEq = loader.load();
        ROOT.setCenter(bddEq);

        // Appel du contrôleur pour charger la page de la table choisie
        GestionBDD<?, ?, ?> controlleur = loader.getController();
        controlleur.afficherTableau(fxml, OptionVisibilite.RESET);
    }

    /**
     * Affichage du message de confirmation avant le lancement d'un traitement.
     * 
     * @param task
     *                     Tâche parente.
     * @param              <T>
     *                     Classe de tâche en cours.
     * @param confirmation
     *                     Texte affiché pour demander la confirmation.
     */
    private <T extends AbstractTask> void alertConfirmation(T task, String confirmation)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Statics.CSS);
        alert.setTitle(task.getTitre());
        alert.setHeaderText(null);
        alert.setContentText(confirmation + Statics.NL + "Etes-vous sur?");

        // Lancement de la tâche sir le bouton OK est presse
        alert.showAndWait().filter(r -> r == ButtonType.OK).ifPresent(r -> LaunchTask.startTask(task));
    }

    /**
     * Mise à jour des titres pour les pages de la base de données.
     * 
     * @param texte
     *              Titre à afficher.
     */
    private void majTitreBDD(String texte)
    {
        MainScreen.majTitre(BDD + texte);
    }

    /**
     * Mise à jour des titres pour les pages des fonctions.
     * 
     * @param texte
     *              Titre à afficher.
     */
    private void majTitreCREATIONS(String texte)
    {
        MainScreen.majTitre(CREATIONS + texte);
    }

    /**
     * Mise à jour des titres pour les pages des fonctions.
     * 
     * @param texte
     *              Titre à afficher.
     */
    private void majTitreSTATS(String texte)
    {
        MainScreen.majTitre(STATISTIQUES + texte);
    }

    /**
     * Mise à jour des titres pour les pages des outils.
     * 
     * @param texte
     *              Titre à afficher.
     */
    private void majTitreOUTILS(String texte)
    {
        MainScreen.majTitre(OUTILS + texte);
    }

    /* ---------- ACCESSEURS ---------- */
}

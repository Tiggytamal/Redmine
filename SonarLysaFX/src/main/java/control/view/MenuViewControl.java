package control.view;

import static utilities.Statics.info;

import java.io.IOException;
import java.util.Optional;

import control.rtc.ControlRTC;
import control.sonar.SonarAPI;
import control.task.CreerListeComposantsTask;
import control.task.MajVuesTask;
import control.task.PurgeSonarTask;
import control.task.AbstractSonarTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;
import view.ConnexionDialog;

public final class MenuViewControl extends AbstractViewControl
{
    /* ---------- ATTIBUTS ---------- */

    // Constantes statiques
    private static final short WIDTHALERT = 640;
    private static final short HEIGHTALERT = 480;
    
    /** Element du ménu lançant les contrôles mensuels */
    @FXML
    private MenuItem mensuel;
    @FXML
    private MenuItem options;
    @FXML
    private MenuItem planificateur;
    @FXML
    private MenuItem rtc;
    @FXML
    private MenuItem autres;
    @FXML
    private MenuItem extraction;
    @FXML
    private MenuItem majVues;
    @FXML
    private MenuItem maintenance;
    @FXML
    private MenuItem suivi;
    @FXML
    private MenuItem appli;
    @FXML
    private MenuItem aide;
    @FXML
    private MenuItem purge;
    @FXML
    private MenuItem majCompos;
    @FXML
    private Button connexion;
    @FXML
    private Button deConnexion;
    @FXML
    private HBox box;

    private BorderPane border;

    /*---------- CONSTRUCTEURS ----------*/

    public MenuViewControl()
    {
        border = MainScreen.getRoot();
    }

    @FXML
    public void initialize()
    {
        box.getChildren().remove(deConnexion);
    }

    /* ---------- METHODES PUBLIQUES ---------- */

    @FXML
    public void openPopup()
    {
        // Création de la popup de connexion
        ConnexionDialog dialog = new ConnexionDialog();

        // Récupération du pseudo et du mdp
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // Contrôle dans Sonar de la validitée
        result.ifPresent(pair -> testMdP(pair.getKey(), pair.getValue()));
    }

    @FXML
    public void deco()
    {
        mensuel.setDisable(true);
        purge.setDisable(true);
        majVues.setDisable(true);
        majCompos.setDisable(true);
        rtc.setDisable(true);
        extraction.setDisable(true);
        planificateur.setDisable(true);
        appli.setDisable(true);
        autres.setDisable(true);
        info.setPseudo(null);
        info.setMotDePasse(null);
        box.getChildren().remove(deConnexion);
        box.getChildren().add(connexion);
        border.setCenter(null);
    }

    /**
     * Permet d'afficher les nouveaux écrans ou de lancer les traitements après confirmation.
     */
    @Override
    public void afficher(ActionEvent event) throws IOException
    {
        String id = "";
        Object source = event.getSource();
        if (source instanceof MenuItem)
            id = ((MenuItem) source).getId();

        if (!info.controle() && !"options".equals(id))
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, Merci de vous connecter");

        switch (id)
        {
            // Chargement des écrans
            case "mensuel":
                load("/view/Mensuel.fxml");
                break;

            case "options":
                load("/view/Options.fxml");
                break;

            case "planificateur":
                load("/view/Planificateur.fxml");
                break;

            case "autres":
                load("/view/AutresVues.fxml");
                break;

            case "maintenance":
                load("/view/Maintenance.fxml");
                break;

            case "suivi":
                load("/view/Suivi.fxml");
                break;

            case "appli":
                load("/view/Applications.fxml");
                break;

            case "rtc":
                load("/view/FichierRTC.fxml");
                break;

            case "extraction":
                load("/view/Extraction.fxml");
                break;

            // Demande confirmations pour traitements
            case "majVues":
                alertConfirmation(new MajVuesTask(), "Cela lancera la mise à jour de toutes les vues Sonar.");
                break;

            case "majCompos":
                alertConfirmation(new CreerListeComposantsTask(), "Cela lancera la mise à jour de tous les composants Sonar.");
                break;

            case "purger":
                alertConfirmation(new PurgeSonarTask(), "Cela lancera la purge des composants Sonar.");
                break;

            default:
                throw new TechnicalException("MenuItem pas géré" + id, null);
        }
    }

    @FXML
    public void aide()
    {
        Alert aidePanel = new Alert(AlertType.NONE);
        aidePanel.initStyle(StageStyle.UTILITY);
        aidePanel.getDialogPane().getStylesheets().add(Statics.CSS);
        aidePanel.setTitle("Aide");
        aidePanel.setHeaderText(null);
        aidePanel.setContentText(null);
        WebView webView = new WebView();
        webView.getEngine().load(getClass().getResource("/aide/menu.html").toString());
        webView.setPrefSize(WIDTHALERT, HEIGHTALERT);
        aidePanel.setResizable(true);
        aidePanel.getDialogPane().setContent(webView);
        aidePanel.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        aidePanel.show();
    }

    /* ---------- METHODES PRIVEES ---------- */

    /**
     * Teste la connexion au serveur Sonar et au serveur RTC
     * 
     * @param pseudo
     * @param mdp
     */
    private void testMdP(String pseudo, String mdp)
    {
        // Sauvegarde informations de connexion
        info.setPseudo(pseudo);
        info.setMotDePasse(mdp);

        // Contrôle connexion RTC et SonarQube
        if (ControlRTC.INSTANCE.connexion() && SonarAPI.INSTANCE.verificationUtilisateur())
        {
            mensuel.setDisable(false);
            purge.setDisable(false);
            majVues.setDisable(false);
            planificateur.setDisable(false);
            autres.setDisable(false);
            appli.setDisable(false);
            majVues.setDisable(false);
            majCompos.setDisable(false);
            extraction.setDisable(false);
            suivi.setDisable(false);
            maintenance.setDisable(false);
            rtc.setDisable(false);
            info.setNom(ControlRTC.INSTANCE.recupNomContributorConnecte());
            deConnexion.setText(info.getNom());

            box.getChildren().remove(connexion);
            box.getChildren().add(deConnexion);
        }
        else
            throw new FunctionalException(Severity.INFO, "Utilisateur incorrect");
    }

    /**
     * Chargement de la nouvelle page en utilisant la ressource en paramètre
     * 
     * @param ressource
     * @throws IOException
     */
    private void load(String ressource) throws IOException
    {
        Node pane = FXMLLoader.load(getClass().getResource(ressource));
        border.setCenter(pane);
    }

    /**
     * Affichage message de confirmation avant le lancement du traitement.
     * 
     * @param task
     * @param confirmation
     */
    private <T extends AbstractSonarTask> void alertConfirmation(T task, String confirmation)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Statics.CSS);
        alert.setTitle(T.TITRE);
        alert.setHeaderText(null);
        alert.setContentText(confirmation + Statics.NL + "Etes-vous sur?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK))
        {
            startTask(task, T.TITRE);
        }
    }

    /* ---------- ACCESSEURS ---------- */
}

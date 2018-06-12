package control.view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import control.rtc.ControlRTC;
import control.sonar.SonarAPI;
import control.task.MajVuesTask;
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

public class MenuViewControl extends ViewControl
{
    /* ---------- ATTIBUTS ---------- */

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
    private MenuItem majvues;
    @FXML
    private MenuItem maintenance;
    @FXML
    private MenuItem suivi;
    @FXML
    private MenuItem aide;
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
        options.setDisable(true);
        rtc.setDisable(true);
        planificateur.setDisable(true);
        autres.setDisable(true);
        Statics.info.setPseudo(null);
        Statics.info.setMotDePasse(null);
        box.getChildren().remove(deConnexion);
        box.getChildren().add(connexion);
        border.setCenter(null);
    }

    @Override
    public void afficher(ActionEvent event) throws IOException
    {
        if (!Statics.info.controle())
            throw new FunctionalException(Severity.ERROR, "Pas de connexion au serveur Sonar, Merci de vous connecter");
        String id = "";
        Object source = event.getSource();
        if (source instanceof MenuItem)
            id = ((MenuItem) source).getId();

        switch (id)
        {
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

            case "rtc":
                load("/view/FichierRTC.fxml");
                break;

            default:
                throw new TechnicalException("MenuItem pas géré" + id, null);
        }
    }

    @FXML
    public void majVues()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(MajVuesTask.TITRE);
        alert.setHeaderText(null);
        alert.setContentText("Cela lancera la mise à jour de toutes les vues Sonar." + Statics.NL + "Etes-vous sur?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK))
            new Thread(new MajVuesTask()).start();
    }

    @FXML
    public void aide()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Aide");
        alert.setHeaderText(null);
        alert.setContentText(null);
        WebView webView = new WebView();
        URL url = this.getClass().getResource("/resources/aide/aide.html");
        webView.getEngine().load(url.toString());
        webView.setPrefSize(640 , 480);
        alert.setResizable(true);
        alert.getDialogPane().setContent(webView);
        alert.show();
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
        Statics.info.setPseudo(pseudo);
        Statics.info.setMotDePasse(mdp);

        // Contrôle connexion RTC et SonarQube
        if (ControlRTC.INSTANCE.connexion() && SonarAPI.INSTANCE.verificationUtilisateur())
        {
            mensuel.setDisable(false);
            options.setDisable(false);
            planificateur.setDisable(false);
            autres.setDisable(false);
            suivi.setDisable(false);
            maintenance.setDisable(false);
            rtc.setDisable(false);

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

    /* ---------- ACCESSEURS ---------- */
}
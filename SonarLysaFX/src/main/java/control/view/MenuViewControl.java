package control.view;

import static utilities.Statics.info;

import java.io.IOException;
import java.util.Optional;

import control.rtc.ControlRTC;
import control.sonar.SonarAPI;
import control.task.MajVuesTask;
import control.task.PurgeSonarTask;
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

    /** Element du m�nu lan�ant les contr�les mensuels */
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
    private MenuItem purge;
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
        // Cr�ation de la popup de connexion
        ConnexionDialog dialog = new ConnexionDialog();

        // R�cup�ration du pseudo et du mdp
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // Contr�le dans Sonar de la validit�e
        result.ifPresent(pair -> testMdP(pair.getKey(), pair.getValue()));
    }

    @FXML
    public void deco()
    {
        mensuel.setDisable(true);
        purge.setDisable(true);
        majvues.setDisable(true);
        rtc.setDisable(true);
        planificateur.setDisable(true);
        autres.setDisable(true);
        info.setPseudo(null);
        info.setMotDePasse(null);
        box.getChildren().remove(deConnexion);
        box.getChildren().add(connexion);
        border.setCenter(null);
    }

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
                throw new TechnicalException("MenuItem pas g�r�" + id, null);
        }
    }

    @FXML
    public void majVues()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Statics.CSS);
        alert.setTitle(MajVuesTask.TITRE);
        alert.setHeaderText(null);
        alert.setContentText("Cela lancera la mise � jour de toutes les vues Sonar." + Statics.NL + "Etes-vous sur?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK))
            new Thread(new MajVuesTask()).start();
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
        webView.setPrefSize(640, 480);
        aidePanel.setResizable(true);
        aidePanel.getDialogPane().setContent(webView);
        aidePanel.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        aidePanel.show();
    }

    @FXML
    public void purger()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Statics.CSS);
        alert.setTitle(PurgeSonarTask.TITRE);
        alert.setHeaderText(null);
        alert.setContentText("Cela lancera la purge des composants Sonar." + Statics.NL + "Etes-vous sur?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK))
        {
            PurgeSonarTask task = new PurgeSonarTask();
            startTask(task, PurgeSonarTask.TITRE);
        }
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

        // Contr�le connexion RTC et SonarQube
        if (ControlRTC.INSTANCE.connexion() && SonarAPI.INSTANCE.verificationUtilisateur())
        {
            mensuel.setDisable(false);
            purge.setDisable(false);
            majvues.setDisable(false);
            planificateur.setDisable(false);
            autres.setDisable(false);
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
     * Chargement de la nouvelle page en utilisant la ressource en param�tre
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
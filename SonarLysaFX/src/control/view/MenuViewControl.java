package control.view;

import static utilities.Statics.proprietesXML;

import java.io.IOException;
import java.util.Optional;

import control.parent.ViewControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import model.enums.TypeParam;
import sonarapi.SonarAPI;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;
import view.ConnexionDialog;

public class MenuViewControl extends ViewControl
{
    /* ---------- ATTIBUTS ---------- */

    /** Element du ménu lançant les contrôles mensuels */
    @FXML
    private MenuItem mensuel;
    @FXML
    private MenuItem applications;
    @FXML
    private MenuItem options;
    @FXML
    private MenuItem planificateur;
    @FXML
    private MenuItem maintenance;
    @FXML
    private Button connexion;
    @FXML
    private Button deConnexion;
    @FXML
    private HBox box;

    /* ---------- METHODES PUBLIQUES ---------- */

    @FXML
    public void initialize()
    {
        box.getChildren().remove(deConnexion);
    }
    
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
        applications.setDisable(true);
        options.setDisable(true);
        planificateur.setDisable(true);
        maintenance.setDisable(true);
        Statics.info.setPseudo(null);
        Statics.info.setMotDePasse(null);
        box.getChildren().remove(deConnexion);
        box.getChildren().add(connexion);
        border.setCenter(null);       
    }

    @FXML
    public void switchToMensuel(ActionEvent event) throws IOException
    {
        load("/view/Mensuel.fxml");
    }

    @FXML
    public void switchToApplications(ActionEvent event) throws IOException
    {
        load("/view/Applications.fxml");
    }

    @FXML
    public void switchToOptions(ActionEvent event) throws IOException
    {
        load("/view/Options.fxml");
    }

    @FXML
    public void switchToPlanificateur(ActionEvent event) throws IOException
    {
        load("/view/Planificateur.fxml");
    }

    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException
    {
        load("/view/Maintenance.fxml");
    }

    /* ---------- METHODES PRIVEES ---------- */

    private void testMdP(String pseudo, String mdp)
    {
        // Jerome rauline Sylvain Jouet Brice Neuzan
        Statics.info.setPseudo(pseudo);
        Statics.info.setMotDePasse(mdp);
        SonarAPI api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), pseudo, mdp);
        if (api.verificationUtilisateur())
        {
            mensuel.setDisable(false);
            applications.setDisable(false);
            options.setDisable(false);
            planificateur.setDisable(false);
            maintenance.setDisable(false);
            box.getChildren().remove(connexion);
            box.getChildren().add(deConnexion);
        }
        else
            throw new FunctionalException(Severity.SEVERITY_INFO, "Utilisateur incorrect");

    }
    /* ---------- ACCESSEURS ---------- */

}

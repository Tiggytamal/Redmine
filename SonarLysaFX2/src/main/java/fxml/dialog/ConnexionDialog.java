package fxml.dialog;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Dialogue permettant de tester la connexion au serveur Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ConnexionDialog extends AbstractBaseDialog<Pair<String, String>, GridPane>
{
    /*---------- ATTRIBUTS ----------*/

    private Node loginButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    /*---------- CONSTRUCTEURS ----------*/

    public ConnexionDialog()
    {
        super("Connexion", "/fxml/dialog/ConnexionDialog.fxml");
        init();
    }

    @Override
    protected void initImpl()
    {
        // Contrôle activation du bouton de connexion
        ButtonType login = new ButtonType("login", ButtonData.OK_DONE);
        pane.getButtonTypes().add(login);
        loginButton = pane.lookupButton(login);
        loginButton.setDisable(true);
        loginButton.setId("loginButton");
        username.textProperty().addListener(new TextListener());
        password.textProperty().addListener(new TextListener());
        affCancel();

        // Focus
        Platform.runLater(username::requestFocus);

        // Transmets le nom d'utilisateur et le mot de passe
        setResultConverter(dialogButton -> {
            if (dialogButton.equals(login))
                return new Pair<>(username.getText(), password.getText());
            return null;
        });
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private class TextListener implements ChangeListener<String>
    {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
        {
            loginButton.setDisable(username.getText().trim().isEmpty() || password.getText().trim().isEmpty());
        }
    }
}

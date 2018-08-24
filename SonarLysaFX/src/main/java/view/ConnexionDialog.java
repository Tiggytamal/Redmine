package view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Dialogue permettant de tester la connexion au serveur Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ConnexionDialog extends Dialog<Pair<String, String>>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final int BASEGAP = 10;
    private static final int TOPINSET = 20;
    private static final int RIGHTINSET = 80;
    
    private Node loginButton;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ConnexionDialog()
    {
        setTitle("Connexion");
        setHeaderText(null);
        getDialogPane().getStylesheets().add("application.css");

        // Boutons
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Gridpane
        GridPane grid = new GridPane();
        grid.setHgap(BASEGAP);
        grid.setVgap(BASEGAP);
        grid.setPadding(new Insets(TOPINSET, RIGHTINSET, BASEGAP, BASEGAP));

        // TextField
        TextField username = new TextField();
        username.setPromptText("Pseudo");
        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");

        grid.add(new Label("Pseudo : "), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Mot de passe : "), 0, 1);
        grid.add(password, 1, 1);

        // Contrôle affichage du bouton
        loginButton = getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        username.textProperty().addListener(new TextListener());
        password.textProperty().addListener(new TextListener());

        // Mise ne place du panel
        getDialogPane().setContent(grid);

        // Focus
        Platform.runLater(username::requestFocus);

        // Converion des données en Pair
        setResultConverter(dialogButton -> {
            if (dialogButton.equals(loginButtonType))
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
            loginButton.setDisable(newValue.trim().isEmpty());
        }
    }
}

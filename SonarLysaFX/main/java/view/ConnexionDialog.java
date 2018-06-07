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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Dialogue permettant de tester la connexion au serveur Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ConnexionDialog extends Dialog<Pair<String, String>>
{
    /*---------- ATTRIBUTS ----------*/

    private Node loginButton;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ConnexionDialog()
    {
        setTitle("Connexion");
        setHeaderText(null);

        // Icône
        setGraphic(new ImageView(this.getClass().getResource("/resources/login.jpg").toString()));

        // Boutons
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Gridpane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 80, 10, 10));

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
            if (dialogButton == loginButtonType)
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
package view;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import control.MacroControl;
import control.XMLControl;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.xml.BanqueXML;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class MainScreen extends Application
{
    /* ---------- ATTIBUTES ---------- */
    
    /* Propri�t�s g�n�rales */

    private Stage stage;
    
    /* Controleur */
    private XMLControl xmlControl;
    
    /* Propri�t�s FXML */

    @FXML
    private ComboBox<String> listeBanques;
    @FXML
    private TextField cosce;
    @FXML
    private TextField incident;
    

    /* ---------- CONSTUCTORS ---------- */
    
    public MainScreen()
    {
        xmlControl = XMLControl.getInstance();
    }
    
    /* ---------- METHODS ---------- */

    @Override
    public void start(Stage primarystage) throws Exception
    {
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
               { 
                   if (e.getCause() instanceof InvocationTargetException && ((InvocationTargetException) e.getCause()).getTargetException() instanceof FunctionalException)
                   {
                       FunctionalException ex1 = (FunctionalException) ((InvocationTargetException) e.getCause()).getTargetException();
                       createAlert(ex1.getSeverity(), ex1, ex1.getMessage());
                   }
                   else
                   {
                       createAlert(Severity.SEVERITY_ERROR, e, e.getMessage());
                   }
               });
        
        
        Parent root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
        Scene scene = new Scene(root);
        stage = primarystage;
        stage.setTitle("Cr�ation TN5B -TN5F");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void initialize()
    {
        listeBanques.getItems().addAll(xmlControl.getListeBanques().keySet());
        listeBanques.getSelectionModel().selectFirst();        
    }
    
    /**
     * M�thode permettant de cr�er un message d'erreur
     * 
     * @param severity
     * @param ex
     * @param detail
     */
    public static void createAlert(Severity severity, Throwable ex, String detail)
    {
        // instance de l'alerte
        Alert alert;

        // Switch sur les s�v�rit�s pour r�cup�rer le type d'alerte
        switch (severity)
        {
            case SEVERITY_ERROR :
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
            case SEVERITY_INFO :
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
            default :
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Une erreur est survenue");
                alert.setHeaderText("Look, an Exception Dialog");
                alert.setContentText(detail);
                break;
        }

        // Cr�ation du message d'exception si celle-ci est fournie.
        if (ex != null && ex instanceof FunctionalException ? ((FunctionalException) ex).isShowException() : true)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Stacktrace :");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
        }

        alert.showAndWait();
    }
    
    @FXML
    public void macro() throws FunctionalException
    {
        String nom = listeBanques.getSelectionModel().getSelectedItem();
        BanqueXML banque = xmlControl.getListeBanques().get(nom);
        
        if (nom.equals("Choisissez une Banque"))
        {
            throw new FunctionalException(Severity.SEVERITY_INFO, "Vous devez choisir un p�le", false);
        }
        if (cosce.getText().isEmpty())
        {
            throw new FunctionalException(Severity.SEVERITY_INFO, "Vous devez rentrez un num�ro de sc�nario", false);
        }
        if (incident.getText().isEmpty())
        {
            throw new FunctionalException(Severity.SEVERITY_INFO, "Vous devez rentrez le num�ro de l'incident", false);
        }
        
        // 2. Cr�ation de la macro.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Macro Quick");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Macro (*.qmc)", "*.qmc"));  
        // Ajout du repertoire par d�fault s'il est valide
        File init = new File(xmlControl.getParam().getUrl());       
        if (init.exists())
            fileChooser.setInitialDirectory(init);
        // Ajout du nom de fichier par d�fault s'il est valide
        if (Pattern.matches("^[A-Za-z0-9-]+\\.[A-Za-z]+$", xmlControl.getParam().getNomFichier()))
            fileChooser.setInitialFileName("TN5B-TN5F");
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null)
        {           
            MacroControl macroControl = new MacroControl(cosce.getText(), banque, incident.getText());
            macroControl.creerMacro(file);
            xmlControl.getParam().setUrl(file.getParentFile().getAbsolutePath());
            xmlControl.getParam().setNomFichier(file.getName());
            xmlControl.saveXML();           
        }
    }
    
    /* ---------- ACCESS ---------- */

}

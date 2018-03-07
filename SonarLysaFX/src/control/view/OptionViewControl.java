package control.view;

import static control.view.MainScreen.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlXML;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import model.enums.TypeCol;
import model.enums.TypeParam;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class OptionViewControl
{
    /*---------- ATTRIBUTS ----------*/

    // Attributs FXML
    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<String> options;
    @FXML
    private GridPane rightSide;
    @FXML
    private Button lotsPic;
    @FXML
    private Button applis;
    @FXML
    private Button clarity;
    @FXML
    private VBox chargementPane;
    @FXML
    private VBox optionsPane;
    @FXML
    private HBox versionsPane;
    @FXML
    private ListView<String> versionsField;
    @FXML
    private Button supprimer;
    @FXML
    private TextField newVersionField;
    @FXML
    private Button ajouter;
    @FXML
    private TextField pathField;
    @FXML
    private TextField pathHistoField;
    @FXML
    private TextField suiviField;
    @FXML
    private TextField datastageField;
    @FXML
    private TextField filtreField;
    @FXML
    private Button sauvegarder;
    @FXML
    private VBox colonnesPane;
    @FXML
    private TextField directionField;
    @FXML
    private TextField departementField;

    // Attributs de classe
    private FileChooser fc;
    private Alert alert;
    private static final int ROW_HEIGHT = 24;
    private Map<TypeParam, String> mapParam;

    /*---------- CONSTRUCTEURS ----------*/

    //253542
    @FXML
    public void initialize()
    {
        // Ajout listener changement de fen�tre d'options
        options.getSelectionModel().selectedItemProperty().addListener((ov, old, newval) -> switchPanel(ov));
        rightSide.getChildren().clear();

        // Initialisation aletre
        alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText("Chargement");
        alert.setHeaderText(null);

        initParametres(proprietesXML.getMapParams());

        initColonnes(proprietesXML.getMapColonnes());
    }

    private void initParametres(Map<TypeParam, String> mapParam)
    {

        // Initialition liste des versions affich�e
        String versionsParam = mapParam.get(TypeParam.VERSIONS);
        
        if (!versionsParam.isEmpty())
        {
            versionsField.getItems().addAll(versionsParam.split("-"));
            versionsField.getItems().sort((o1, o2) -> o1.compareTo(o2));
        }

        // Mise � jour automatique de la liste des versions
        versionsField.getSelectionModel().selectFirst();
        versionsField.setPrefHeight((double) versionsField.getItems().size() * ROW_HEIGHT + 2);
        versionsField.getItems().addListener(
                (ListChangeListener.Change<? extends String> c) -> versionsField.setPrefHeight((double) versionsField.getItems().size() * ROW_HEIGHT + 2));

        // Intialisation des TextField depuis le fichier de param�tre
        pathField.setText(mapParam.get(TypeParam.ABSOLUTEPATH).replace("\\\\", "\\"));
        suiviField.setText(mapParam.get(TypeParam.NOMFICHIER));
        datastageField.setText(mapParam.get(TypeParam.NOMFICHIERDATASTAGE));
        filtreField.setText(mapParam.get(TypeParam.FILTREDATASTAGE));
        pathHistoField.setText(mapParam.get(TypeParam.ABSOLUTEPATHHISTO).replace("\\\\", "\\"));
        
    }
    
    private void initColonnes(Map<TypeCol, String> mapColonnes)
    {
        departementField.setText(mapColonnes.get(TypeCol.DEPARTEMENT));
        
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void chargerLotsPic() throws InvalidFormatException, IOException, JAXBException
    {
        fc = new FileChooser();
        fc.setTitle("Charger Lots Pic");
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showOpenDialog(splitPane.getScene().getWindow());
        if (file != null)
        {
            alert.show();
            ControlXML control = new ControlXML();
            control.recupLotsPicDepuisExcel(file);
            alert.setContentText("Chargement lots Pic effectu�");
        }
    }

    public void chargerApplis() throws InvalidFormatException, IOException, JAXBException
    {
        fc = new FileChooser();
        fc.setTitle("Charger Applications");
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showOpenDialog(splitPane.getScene().getWindow());
        if (file != null)
        {
            alert.show();
            ControlXML control = new ControlXML();
            control.recupListeAppsDepuisExcel(file);
            alert.setContentText("Chargement Applications effectu�");
        }
    }

    public void chargerClarity() throws InvalidFormatException, IOException, JAXBException
    {
        fc = new FileChooser();
        fc.setTitle("Charger Referentiel Clarity");
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showOpenDialog(splitPane.getScene().getWindow());
        if (file != null)
        {
            alert.show();
            ControlXML control = new ControlXML();
            control.recupInfosClarityDepuisExcel(file);
            alert.setContentText("Chargement Clarity effectu�");
        }
    }

    /**
     * Supprime la version selectionn�e de la liste
     * 
     * @throws JAXBException
     */
    public void suppVersion()
    {
        // Suppression de la version de la liste affich�e
        int index = versionsField.getSelectionModel().getSelectedIndex();
        ObservableList<String> liste = versionsField.getItems();
        if (index != -1)
        {
            liste.remove(index);
            if (!liste.isEmpty())
                versionsField.getSelectionModel().select(index - 1);
        }
        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    /**
     * Ajoute une nouvelle version � la liste et au fichier de param�tre
     * 
     * @throws JAXBException
     */
    public void ajouterVersion()
    {
        String version = newVersionField.getText();
        ObservableList<String> liste = versionsField.getItems();
        // On contr�le la bonne structure du nom de la version et on ne cr�e pas de doublon
        if (version.matches("^E[0-9][0-9]") && !liste.contains(version))
        {
            liste.add(version);
        }
        else
        {
            throw new FunctionalException(Severity.SEVERITY_ERROR, "La version doit �tre de la forme ^E[0-9][0-9]");
        }
        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    public void sauvegarder() throws JAXBException
    {
        // Sauvegarde versions
        StringBuilder builder = new StringBuilder();
        ObservableList<String> liste = versionsField.getItems();
        for (int i = 0; i < liste.size(); i++)
        {
            builder.append(liste.get(i));
            if (i < liste.size() - 1)
                builder.append("-");
        }
        mapParam.put(TypeParam.VERSIONS, builder.toString());

        // Sauvegarde des autres param�tres
        String path = pathField.getText();
        if (path != null && !path.isEmpty())
            mapParam.put(TypeParam.ABSOLUTEPATH, path.replace("\\", "\\\\"));

        String suivi = suiviField.getText();
        if (suivi != null && !suivi.isEmpty())
            mapParam.put(TypeParam.NOMFICHIER, suivi);

        String datastage = datastageField.getText();
        if (datastage != null && !datastage.isEmpty())
            mapParam.put(TypeParam.NOMFICHIERDATASTAGE, datastage);

        String filtre = filtreField.getText();
        if (filtre != null && !filtre.isEmpty())
            mapParam.put(TypeParam.FILTREDATASTAGE, filtre);

        String pathHisto = pathHistoField.getText();
        if (pathHisto != null && !pathHisto.isEmpty())
            mapParam.put(TypeParam.ABSOLUTEPATHHISTO, pathHisto.replace("\\", "\\\\"));

        // Enregistrement param�tres
        new ControlXML().saveParam(proprietesXML);
    }

    /*---------- METHODES PRIVEES ----------*/

    private void switchPanel(ObservableValue<? extends String> ov)
    {
        if (ov.getValue().equals("Chargement fichiers"))
        {
            rightSide.getChildren().clear();
            rightSide.getChildren().add(chargementPane);
        }
        if (ov.getValue().equals("Param�tres"))
        {
            rightSide.getChildren().clear();
            rightSide.getChildren().add(optionsPane);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
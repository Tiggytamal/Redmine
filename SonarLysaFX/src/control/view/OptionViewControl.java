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
import model.enums.TypeKey;
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
    @FXML
    private TextField serviceField;
    @FXML
    private TextField respServiceField;
    @FXML
    private TextField clarityField;
    @FXML
    private TextField libelleField;
    @FXML
    private TextField cpiField;
    @FXML
    private TextField editionField;
    @FXML
    private TextField lotField;
    @FXML
    private TextField etatLotField;
    @FXML
    private TextField anomalieField;
    @FXML
    private TextField dateCreaField;
    @FXML
    private TextField etatAnoField;
    @FXML
    private TextField dateRelField;
    @FXML
    private TextField remarqueField;
    @FXML
    private TextField securiteField;
    @FXML
    private TextField versionField;

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
        directionField.setText(mapColonnes.get(TypeCol.DIRECTION));
        departementField.setText(mapColonnes.get(TypeCol.DEPARTEMENT));
        serviceField.setText(mapColonnes.get(TypeCol.SERVICE));
        respServiceField.setText(mapColonnes.get(TypeCol.RESPSERVICE));
        clarityField.setText(mapColonnes.get(TypeCol.CLARITY));
        libelleField.setText(mapColonnes.get(TypeCol.LIBELLE));
        cpiField.setText(mapColonnes.get(TypeCol.CPI));
        editionField.setText(mapColonnes.get(TypeCol.EDITION));
        lotField.setText(mapColonnes.get(TypeCol.LOT));
        etatLotField.setText(mapColonnes.get(TypeCol.ENV));
        anomalieField.setText(mapColonnes.get(TypeCol.ANOMALIE));
        dateCreaField.setText(mapColonnes.get(TypeCol.DATECREATION));
        dateRelField.setText(mapColonnes.get(TypeCol.DATERELANCE));
        etatAnoField.setText(mapColonnes.get(TypeCol.ETAT));
        remarqueField.setText(mapColonnes.get(TypeCol.REMARQUE));
        securiteField.setText(mapColonnes.get(TypeCol.SECURITE));
        versionField.setText(mapColonnes.get(TypeCol.VERSION));
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

        saveText(suiviField, mapParam, TypeParam.NOMFICHIER);

        saveText(datastageField, mapParam, TypeParam.NOMFICHIERDATASTAGE);

        saveText(filtreField, mapParam, TypeParam.FILTREDATASTAGE);

        String pathHisto = pathHistoField.getText();
        if (pathHisto != null && !pathHisto.isEmpty())
            mapParam.put(TypeParam.ABSOLUTEPATHHISTO, pathHisto.replace("\\", "\\\\"));

        // Enregistrement param�tres
        new ControlXML().saveParam(proprietesXML);
    }
    
    
    public void saveCols() throws JAXBException
    {
        Map<TypeCol, String> mapCols = proprietesXML.getMapColonnes();
        saveText(directionField, mapCols, TypeCol.DIRECTION);
        saveText(serviceField, mapCols, TypeCol.SERVICE);
        saveText(departementField, mapCols, TypeCol.DEPARTEMENT);
        saveText(respServiceField, mapCols, TypeCol.RESPSERVICE);
        saveText(clarityField, mapCols, TypeCol.CLARITY);
        saveText(libelleField, mapCols, TypeCol.LIBELLE);
        saveText(cpiField, mapCols, TypeCol.CPI);
        saveText(editionField, mapCols, TypeCol.EDITION);
        saveText(lotField, mapCols, TypeCol.LOT);
        saveText(etatLotField, mapCols, TypeCol.ENV);
        saveText(anomalieField, mapCols, TypeCol.ANOMALIE);
        saveText(dateCreaField, mapCols, TypeCol.DATECREATION);
        saveText(dateRelField, mapCols, TypeCol.DATERELANCE);
        saveText(etatAnoField, mapCols, TypeCol.ETAT);
        saveText(remarqueField, mapCols, TypeCol.REMARQUE);
        saveText(securiteField, mapCols, TypeCol.SECURITE);
        saveText(versionField, mapCols, TypeCol.VERSION);
        
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
        if (ov.getValue().equals("Nom Colonnes"))
        {
            rightSide.getChildren().clear();
            rightSide.getChildren().add(colonnesPane);
        }
    }
    
    private <T extends TypeKey> void saveText(TextField textField, Map<T, String> map, T clef)
    {
        String text = textField.getText();
        if (text != null && !text.isEmpty())
            map.put(clef, text);
    }

    /*---------- ACCESSEURS ----------*/
}
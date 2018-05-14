package control.view;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.xml.bind.JAXBException;

import control.xml.ControlXML;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import model.enums.TypeBool;
import model.enums.TypeCol;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeColSuivi;
import model.enums.TypeKey;
import model.enums.Param;
import model.enums.ParamSpec;
import utilities.Statics;
import utilities.TechnicalException;
import view.BooleanView;
import view.ColonneView;
import view.ParamListView;
import view.ParamView;

public class OptionViewControl extends ViewControl
{
    /*---------- ATTRIBUTS ----------*/

    // Attributs FXML

    @FXML
    private GridPane rightSide;
    @FXML
    private VBox chargementPane;
    @FXML
    private ScrollPane optionsPane;
    @FXML
    private ScrollPane optionsPane2;
    @FXML
    private ScrollPane colonnesPane;
    @FXML
    private TreeView<String> options;
    @FXML
    private VBox colonnesBox;
    @FXML
    private VBox booleanBox;
    @FXML
    private VBox paramsBox;
    @FXML
    private VBox paramsBox2;
    @FXML
    private Label labelDescAnoRTC;
    @FXML
    private Label labelSecuAnoRTC;
    @FXML
    private TextArea descAnoRTC;
    @FXML
    private TextArea secuAnoRTC;

    // Attributs de classe

    private Alert alert;

    private Map<Param, String> mapParams;
    private Map<TypeBool, Boolean> mapParamsBool;
    private Map<ParamSpec, String> mapParamsSpec;
    private ControlXML control;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        // Ajout listener changement de fenêtre d'options
        options.getSelectionModel().selectedItemProperty().addListener((ov, old, newval) -> switchPanel(ov));
        rightSide.getChildren().clear();

        // Initialisation alerte
        alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        mapParams = proprietesXML.getMapParams();
        mapParamsBool = proprietesXML.getMapParamsBool();
        mapParamsSpec = proprietesXML.getMapParamsSpec();
        control = new ControlXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Controle de l'affichage des options en utilisant la TreeView
     * 
     * @param ov
     */
    protected void switchPanel(ObservableValue<? extends TreeItem<String>> ov)
    {
        ObservableList<Node> root = rightSide.getChildren();
        root.clear();

        switch (ov.getValue().getValue())
        {
            case "Chargement fichiers":
                root.add(chargementPane);
                break;

            case "Paramètres":
                afficherParams();
                root.add(optionsPane);
                break;
            
            case "Autres Paramètres":
                afficherParamsAutres();
                root.add(optionsPane2);
                break;

            case "SuiviQualité":
                afficherColonnes(TypeColSuivi.class, root);
                break;

            case "Clarity":
                afficherColonnes(TypeColClarity.class, root);
                break;

            case "Chef de Service":
                afficherColonnes(TypeColChefServ.class, root);
                break;

            case "Lots Pic":
                afficherColonnes(TypeColPic.class, root);
                break;

            case "Codification Editions":
                afficherColonnes(TypeColEdition.class, root);
                break;

            case "Applications":
                afficherColonnes(TypeColApps.class, root);
                break;

            case "Nom Colonnes":
                break;

            default:
                throw new TechnicalException("TreeItem pas géré" + ov.getValue().getValue(), null);
        }
    }

    @FXML
    public void chargerFichier(ActionEvent event)
    {
        String id = "";
        Object source = event.getSource();
        if (source instanceof Node)
            id = ((Node) source).getId();

        switch (id)
        {
            case "lotsPic":
                charger("Lots Pic", file -> control.recupLotsPicDepuisExcel(file));
                break;

            case "apps":
                charger("Applications", file -> control.recupListeAppsDepuisExcel(file));
                break;

            case "clarity":
                charger("Referentiel Clarity", file -> control.recupInfosClarityDepuisExcel(file));
                break;

            case "chefSrev":
                charger("Chefs de Service", file -> control.recupChefServiceDepuisExcel(file));
                break;

            case "edition":
                charger("Editions CDM", file -> control.recupEditionDepuisExcel(file));
                break;

            default:
                break;
        }
    }


    /**
     * Sauvegarde les paramètres String et booléens
     * 
     * @throws JAXBException
     */
    public void saveParams() throws JAXBException
    {
        // Sauvegarde des autres paramètres
        for (Node node : paramsBox.getChildren())
        {
            if (node instanceof ParamView)
            {
                ParamView view = (ParamView) node;
                saveText(view.getField(), mapParams, view.getType());
            }
        }

        // Sauvegarde des paramètres booléens
        for (Node node : booleanBox.getChildren())
        {
            if (node instanceof BooleanView)
            {
                BooleanView view = (BooleanView) node;
                mapParamsBool.put(view.getType(), view.getField().isSelected());
            }
        }

        // Enregistrement paramètres
        new ControlXML().saveParam(proprietesXML);
    }
    
    /**
     * Sauvegarde les paramètres spéciaux
     * 
     * @throws JAXBException
     */
    public void saveParamsSpec() throws JAXBException
    {
        for (Node node : paramsBox2.getChildren())
        {
            if (node instanceof ParamListView)
                ((ParamListView)node).sauverValeurs();
        }
        
        proprietesXML.getMapParamsSpec().put(ParamSpec.TEXTEDEFECT, descAnoRTC.getText());
        proprietesXML.getMapParamsSpec().put(ParamSpec.TEXTESECURITE, secuAnoRTC.getText());

        // Enregistrement paramètres
        new ControlXML().saveParam(proprietesXML);
    }

    /**
     * @throws JAXBException
     */
    public <T extends Enum<T> & TypeCol> void saveCols() throws JAXBException
    {
        for (Node node : colonnesBox.getChildren())
        {
            if (node instanceof ColonneView)
            {
                @SuppressWarnings ("unchecked")
                ColonneView<T> view = (ColonneView<T>) node;
                @SuppressWarnings ("unchecked")
                Map<T, String> mapCols = proprietesXML.getMap(view.getType().getDeclaringClass());
                saveText(view.getField(), mapCols, view.getType());
            }
        }

        new ControlXML().saveParam(proprietesXML);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Affichge les colonnes du fichier choisi dans les options
     * 
     * @param typeCol
     * @param root
     */
    private <T extends Enum<T> & TypeCol> void afficherColonnes(Class<T> typeCol, ObservableList<Node> root)
    {
        // Nettoyage de l'affichage
        colonnesBox.getChildren().clear();

        // Récupération de la map correspondante au type de fichier et affichage des colonnes
        for (Object objet : proprietesXML.getMap(typeCol).entrySet())
        {
            @SuppressWarnings ({ "rawtypes", "unchecked" })
            Map.Entry<T, String> entry = (Map.Entry) objet;
            ColonneView<T> cv = new ColonneView<>(entry.getKey(), entry.getValue());
            colonnesBox.getChildren().add(cv);
        }
        root.add(colonnesPane);
    }

    /**
     * Affichage des paramètres
     */
    private void afficherParams()
    {
        paramsBox.getChildren().clear();
        booleanBox.getChildren().clear();

        // Récupération de la map correspondante au type de fichier et affichage des colonnes.
        for (Map.Entry<Param, String> entry : mapParams.entrySet())
        {
            ParamView pv = new ParamView(entry.getKey(), entry.getValue());
            paramsBox.getChildren().add(pv);
        }

        // Affichage de tous les paramètres de type booléens
        for (Entry<TypeBool, Boolean> entry : mapParamsBool.entrySet())
        {
            BooleanView pv = new BooleanView(entry.getKey(), entry.getValue());
            booleanBox.getChildren().add(pv);
        }
    }
    
    /**
     * Affichage des paramètres autres
     */
    private void afficherParamsAutres()
    {      
        paramsBox2.getChildren().clear();
        paramsBox2.getChildren().add(new ParamListView(ParamSpec.VERSIONS));
        paramsBox2.getChildren().add(new ParamListView(ParamSpec.MEMBRESJAVA));
        paramsBox2.getChildren().add(new ParamListView(ParamSpec.MEMBRESDTATSTAGE));
        labelDescAnoRTC.setText(ParamSpec.TEXTEDEFECT.toString());
        descAnoRTC.setText(mapParamsSpec.get(ParamSpec.TEXTEDEFECT).replace("\\n", Statics.NL));
        labelSecuAnoRTC.setText(ParamSpec.TEXTESECURITE.toString());
        secuAnoRTC.setText(mapParamsSpec.get(ParamSpec.TEXTESECURITE).replace("\\n", Statics.NL));       
    }

    /**
     * Sauvegarde la valeur d'un champ dans la map des porpriétés
     * 
     * @param textField
     *            Champ à sauvegarder
     * @param map
     *            map des paramètres
     * @param clef
     *            clef de la map. {@code model.enums.TypeKey}
     */
    private <T extends TypeKey> void saveText(TextField textField, Map<T, String> map, T clef)
    {
        String text = textField.getText();
        if (text != null && !text.isEmpty())
            map.put(clef, text.replace("\\", "\\\\"));
    }

    /**
     * Chargement d'un fichier Excel. Paramétrage en fonction de la méthode à utiliser pour chaque type de fichier.
     * 
     * @param texte
     * @param fonction
     */
    private void charger(String texte, Consumer<File> fonction)
    {
        File file = getFileFromFileChooser("Charger " + texte);
        Platform.runLater(() -> {
            fonction.accept(file);
            alert.setContentText("Chargement " + texte + " effectué");
            alert.show();
        });
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        // Gestion de l'affichage délégué à une autre méthode à cause de l'utilisation de la TreeView
    }

    /*---------- ACCESSEURS ----------*/
}
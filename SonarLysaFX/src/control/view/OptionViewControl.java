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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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
import model.enums.TypeParam;
import model.enums.TypeParamSpec;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.enums.Severity;
import view.BooleanView;
import view.ColonneView;
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
    private ListView<String> versionsField;
    @FXML
    private TextField newVersionField;
    @FXML
    private ListView<String> respJavaField;
    @FXML
    private TextField newRespJavaField;
    @FXML
    private ListView<String> respDatastageField;
    @FXML
    private TextField newRespDatastageField;
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

    // Attributs de classe

    private Alert alert;
    private static final int ROW_HEIGHT = 24;
    private Map<TypeParam, String> mapParams;
    private Map<TypeParamSpec, String> mapParamsSpec;
    private Map<TypeBool, Boolean> mapParamsBool;
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
    private void switchPanel(ObservableValue<? extends TreeItem<String>> ov)
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
     * Supprime la version selectionnée de la liste
     */
    public void suppVersion()
    {
        // Suppression de la version de la liste affichée
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
     * Ajoute une nouvelle version à la liste et au fichier de paramètre
     * 
     */
    public void ajouterVersion()
    {
        String version = newVersionField.getText();
        ObservableList<String> liste = versionsField.getItems();
        // On contrôle la bonne structure du nom de la version et on ne crée pas de doublon
        if (version.matches("^E[0-9][0-9]") && !liste.contains(version))
        {
            liste.add(version);
        }
        else
        {
            throw new FunctionalException(Severity.ERROR, "La version doit être de la forme ^E[0-9][0-9]");
        }
        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    /**
     * @throws JAXBException
     */
    public void sauvegarder() throws JAXBException
    {
        // Sauvegarde versions
        StringBuilder builder = new StringBuilder();
        ObservableList<String> liste = versionsField.getItems();
        for (int i = 0; i < liste.size(); i++)
        {
            builder.append(liste.get(i));
            if (i < liste.size() - 1)
                builder.append(";");
        }
        mapParamsSpec.put(TypeParamSpec.VERSIONS, builder.toString());

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

        // Récupération de la map correspondante au type de fichier et affichage des colonnes. On saute juste les
        // versions qui sont gérées différement
        for (Map.Entry<TypeParam, String> entry : mapParams.entrySet())
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
        gestionAffListView(mapParamsSpec.get(TypeParamSpec.VERSIONS), versionsField);
        gestionAffListView(mapParamsSpec.get(TypeParamSpec.MEMBRESJAVA), respJavaField);
        gestionAffListView(mapParamsSpec.get(TypeParamSpec.MEMBRESDTATSTAGE), respDatastageField);
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
    
    private void gestionAffListView(String infosDepuisMap, ListView<String> listView)
    {
        if (infosDepuisMap != null && !infosDepuisMap.isEmpty())
        {
            listView.getItems().clear();
            listView.getItems().addAll(infosDepuisMap.split(";"));
            listView.getItems().sort((o1, o2) -> o1.compareTo(o2));
        }

        // Mise à jour automatique de la liste des versions
        listView.getSelectionModel().selectFirst();
        double taille = (double) listView.getItems().size() * ROW_HEIGHT + 2;
        listView.setPrefHeight(taille);
        listView.getItems().addListener((ListChangeListener.Change<? extends String> c) -> listView.setPrefHeight(taille));
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        // Gestion de l'affichage délégué à une autre méthode à cause de l'utilisation de la ViewList
    }

    /*---------- ACCESSEURS ----------*/
}
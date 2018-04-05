package control.view;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.bind.JAXBException;

import control.ControlXML;
import control.parent.ViewControl;
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
import model.enums.TypeCol;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeColSuivi;
import model.enums.TypeKey;
import model.enums.TypeParam;
import utilities.FunctionalException;
import utilities.enums.Severity;
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
    private ListView<String> versionsField;
    @FXML
    private TextField newVersionField;
    @FXML
    private ScrollPane colonnesPane;
    @FXML
    private TreeView<String> options;
    @FXML
    private VBox colonnesBox;
    @FXML
    private VBox paramsBox;

    // Attributs de classe
    
    private Alert alert;
    private static final int ROW_HEIGHT = 24;
    private Map<TypeParam, String> mapParam;
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
        alert.setContentText("Chargement");
        alert.setHeaderText(null);
        mapParam = proprietesXML.getMapParams();
        control = new ControlXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    private void switchPanel(ObservableValue<? extends TreeItem<String>> ov)
    {
        ObservableList<Node> root = rightSide.getChildren();
        
        switch (ov.getValue().getValue())
        {
            case "Chargement fichiers" :
                root.clear();
                root.add(chargementPane);
                break;
                
            case "Paramètres" :
                root.clear();
                afficherParams();
                root.add(optionsPane);
                break;
                
            case "SuiviQualité" :
                afficherColonnes(TypeColSuivi.class, root);
                break;
                
            case "Clarity" :
                afficherColonnes(TypeColClarity.class, root);
                break;
                
            case "Chef de Service" :
                afficherColonnes(TypeColChefServ.class, root);
                break;
                
            case "Lots Pic" :
                afficherColonnes(TypeColPic.class, root);
                break;
                
            case "Codification Editions" :
                afficherColonnes(TypeColEdition.class, root);
                break;
                
            default:
                break;
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
     * 
     * @throws JAXBException
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
     * @throws JAXBException
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
            throw new FunctionalException(Severity.SEVERITY_ERROR, "La version doit être de la forme ^E[0-9][0-9]");
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
                builder.append("-");
        }
        mapParam.put(TypeParam.VERSIONS, builder.toString());
        
        // Sauvegarde des autres paramètres
        for (Node node : paramsBox.getChildren())
        {
            if (node instanceof ParamView)
            {
                ParamView view = (ParamView) node;
                saveText(view.getField(), mapParam, view.getTypeParam());
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
                @SuppressWarnings("unchecked")
                ColonneView<T> view = (ColonneView<T>) node;
                Map<T, String> mapCols = proprietesXML.getMapCols(view.getTypeCol().getDeclaringClass());
                saveText(view.getField(), mapCols, view.getTypeCol());
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
        root.clear();
        colonnesBox.getChildren().clear();
        
        // Récupération de la map correspondante au type de fichier et affichage des colonnes
        for (Map.Entry<T, String> entry : proprietesXML.getMapCols(typeCol).entrySet())
        {
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
        
        // Initialition liste des versions affichée
        String versionsParam = mapParam.get(TypeParam.VERSIONS);

        if (versionsParam != null && !versionsParam.isEmpty())
        {
            versionsField.getItems().clear();
            versionsField.getItems().addAll(versionsParam.split("-"));
            versionsField.getItems().sort((o1, o2) -> o1.compareTo(o2));
        }

        // Mise à jour automatique de la liste des versions
        versionsField.getSelectionModel().selectFirst();
        double taille = (double) versionsField.getItems().size() * ROW_HEIGHT + 2;
        versionsField.setPrefHeight(taille);
        versionsField.getItems().addListener((ListChangeListener.Change<? extends String> c) -> versionsField.setPrefHeight(taille));

        
        // Récupération de la map correspondante au type de fichier et affichage des colonnes. On saute juste les versions qui sont gérées différement
        for (Map.Entry<TypeParam, String> entry : mapParam.entrySet())
        {
            if (entry.getKey() == TypeParam.VERSIONS)
                continue;
            
            ParamView pv = new ParamView(entry.getKey(), entry.getValue());
            paramsBox.getChildren().add(pv);
        }               
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
        alert.show();
        File file = getFileFromFileChooser("Charger " + texte);
        fonction.accept(file);
        alert.setContentText("Chargement " + texte + " effectué");
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        // Gestion de l'affichage délégué ç une autre méthode à cause de l'utilisation de la ViewList
    }

    /*---------- ACCESSEURS ----------*/
}
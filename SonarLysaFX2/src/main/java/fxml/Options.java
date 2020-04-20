package fxml;

import static utilities.Statics.EMPTY;
import static utilities.Statics.propPersoXML;
import static utilities.Statics.proprietesXML;
import static utilities.Utilities.creerRaccourciClavierBoutonCtrlS;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import application.MainScreen;
import control.parsing.ControlXML;
import dao.DaoFactory;
import fxml.node.ColonneIndiceView;
import fxml.node.ColonneView;
import fxml.node.ParamBoolView;
import fxml.node.ParamListView;
import fxml.node.ParamTextView;
import fxml.node.ParamView;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import model.Colonne;
import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.Edition;
import model.bdd.ProjetClarity;
import model.enums.ColAppliDir;
import model.enums.ColChefServ;
import model.enums.ColClarity;
import model.enums.ColCompo;
import model.enums.ColEdition;
import model.enums.ColPic;
import model.enums.ColR;
import model.enums.ColVul;
import model.enums.ColW;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypeKey;
import model.enums.TypeParamSpec;
import utilities.AbstractToStringImpl;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Gestion de l'affichage des options de l'application.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class Options extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    public static final SimpleBooleanProperty disablePerso = new SimpleBooleanProperty(false);

    private static final String ENTETE = "Outils/Options/";

    // Attributs FXML

    @FXML
    private GridPane rightSide;
    @FXML
    private VBox chargementPane;
    @FXML
    private ScrollPane paramsPane;
    @FXML
    private ScrollPane paramsAutresPane;
    @FXML
    private ScrollPane paramsPersoPane;
    @FXML
    private ScrollPane colonnesPane;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private VBox colonnesBox;
    @FXML
    private VBox booleanBox;
    @FXML
    private VBox paramsBox;
    @FXML
    private VBox paramsAutresBox;
    @FXML
    private VBox paramsPersoBox;
    @FXML
    private Button buttonSaveParams;
    @FXML
    private Button buttonSaveParamsSpec;
    @FXML
    private Button buttonSaveParamsPerso;
    @FXML
    private Button buttonSaveCols;

    // Attributs de classe

    private Alert alert;

    private Map<Param, String> mapParams;
    private Map<Param, String> mapParamsPerso;
    private Map<ParamBool, Boolean> mapParamsBool;
    private Map<ParamSpec, String> mapParamsSpec;
    private Map<ParamSpec, String> mapParamsPersoSpec;
    private ControlXML controlXML;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        // Ajout listener changement de fenêtre d'options
        treeView.getSelectionModel().selectedItemProperty().addListener((ov, old, newval) -> switchPanel(ov));
        rightSide.getChildren().clear();

        // Initialisation alerte
        alert = new Alert(AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        mapParams = proprietesXML.getMapParams();
        mapParamsPerso = propPersoXML.getParams();
        mapParamsSpec = proprietesXML.getMapParamsSpec();
        mapParamsPersoSpec = propPersoXML.getParamsSpec();
        mapParamsBool = proprietesXML.getMapParamsBool();
        controlXML = new ControlXML();
    }

    /*---------- METHODES AFFICHAGE ----------*/

    /**
     * Controle de l'affichage des options en utilisant la TreeView.
     * 
     * @param ov
     *           Valeur du TreeItem selectionné.
     */
    public void switchPanel(ObservableValue<? extends TreeItem<String>> ov)
    {
        ObservableList<Node> root = rightSide.getChildren();
        root.clear();

        String value = ov.getValue().getValue();
        MainScreen.majTitre(ENTETE + value);

        switch (value)
        {
            case "Chargement fichiers":
                root.add(chargementPane);
                break;

            case "Paramètres":
                afficherParams();
                root.add(paramsPane);
                break;

            case "Paramètres personnels":
                if (!disablePerso.get())
                {
                    afficherParamsPerso();
                    root.add(paramsPersoPane);
                }
                break;

            case "Autres Paramètres":
                afficherParamsAutres();
                root.add(paramsAutresPane);
                break;

            // Affichage des paramètres des fichiers en lecture
            case "Clarity":
                afficherColonnes(ColClarity.class, root);
                break;

            case "Chef de Service":
                afficherColonnes(ColChefServ.class, root);
                break;

            case "Codification Editions":
                afficherColonnes(ColEdition.class, root);
                break;

            case "Appli. / Directions":
                afficherColonnes(ColAppliDir.class, root);
                break;

            case "Fichier Pic":
                afficherColonnes(ColPic.class, root);
                break;

            // Affichage des paramètres des fichiers en écriture
            case "Extract. Vul.":
                afficherColonnesIndice(ColVul.class, root);
                break;

            case "Extract. Composants":
                afficherColonnesIndice(ColCompo.class, root);
                break;

            // Evite plantage en cas de clic sur le sous-menu
            case "Nom Colonnes":
                MainScreen.majTitre(ENTETE);
                break;

            default:
                throw new TechnicalException("TreeItem pas géré : " + ov.getValue().getValue(), null);
        }
    }

    private void afficherParams()
    {
        paramsBox.getChildren().clear();
        booleanBox.getChildren().clear();
        creerRaccourciClavierBoutonCtrlS(buttonSaveParams, rightSide.getScene());

        // Affichage de la liste des paramètres
        for (Param param : Param.values())
        {
            if (!param.isPerso())
            {
                ParamView pv = new ParamView(param, mapParams.computeIfAbsent(param, p -> EMPTY));
                paramsBox.getChildren().add(pv);
            }
        }

        // Affichage de tous les paramètres de type booleens
        for (ParamBool param : ParamBool.values())
        {
            ParamBoolView pv = new ParamBoolView(param, mapParamsBool.computeIfAbsent(param, p -> Boolean.FALSE));
            booleanBox.getChildren().add(pv);
        }
    }

    private void afficherParamsPerso()
    {
        paramsPersoBox.getChildren().clear();
        creerRaccourciClavierBoutonCtrlS(buttonSaveParamsPerso, rightSide.getScene());

        // Affichage de la liste des paramètres
        for (Param param : Param.values())
        {
            if (param.isPerso())
            {
                ParamView pv = new ParamView(param, mapParamsPerso.computeIfAbsent(param, p -> EMPTY));
                paramsPersoBox.getChildren().add(pv);
            }
        }

        // Affichage de la liste des paramètres
        for (ParamSpec param : ParamSpec.values())
        {
            if (param.isPerso())
            {
                if (param.getType() == TypeParamSpec.TEXTAREA)
                    paramsPersoBox.getChildren().add(new ParamTextView(param));
                else if (param.getType() == TypeParamSpec.LISTVIEWNOM)
                    paramsPersoBox.getChildren().add(new ParamListView(param));
                else
                    throw new TechnicalException("control.view.OptionViewControl.afficherParamsAutres - TypeParamSpec non géré : " + param.getType(), null);
            }
        }
    }

    private void afficherParamsAutres()
    {
        paramsAutresBox.getChildren().clear();
        creerRaccourciClavierBoutonCtrlS(buttonSaveParamsSpec, rightSide.getScene());

        for (ParamSpec param : ParamSpec.values())
        {
            if (!param.isPerso())
            {
                if (param.getType() == TypeParamSpec.TEXTAREA)
                    paramsAutresBox.getChildren().add(new ParamTextView(param));
                else if (param.getType() == TypeParamSpec.LISTVIEWNOM)
                    paramsAutresBox.getChildren().add(new ParamListView(param));
                else
                    throw new TechnicalException("control.view.OptionViewControl.afficherParamsAutres - TypeParamSpec non géré : " + param.getType(), null);
            }
        }
    }

    private <T extends Enum<T> & ColR> void afficherColonnes(Class<T> col, List<Node> root)
    {
        // Nettoyage de l'affichage
        colonnesBox.getChildren().clear();

        // Récupération de la map correspondante au type de fichier et affichage des colonnes
        for (Map.Entry<T, String> entry : proprietesXML.getEnumMapColR(col).entrySet())
        {
            ColonneView<T> cv = new ColonneView<>(entry.getKey(), entry.getValue());
            colonnesBox.getChildren().add(cv);
        }
        root.add(colonnesPane);
        creerRaccourciClavierBoutonCtrlS(buttonSaveCols, rightSide.getScene());
    }

    private <T extends Enum<T> & ColW> void afficherColonnesIndice(Class<T> col, List<Node> root)
    {
        // Nettoyage de l'affichage
        colonnesBox.getChildren().clear();

        // Récupération de la map correspondante au type de fichier et affichage des colonnes
        for (Map.Entry<T, Colonne> entry : proprietesXML.getEnumMapColW(col).entrySet())
        {
            ColonneIndiceView<T> cv = new ColonneIndiceView<>(entry.getKey(), entry.getValue().getNom(), entry.getValue().getIndice());
            colonnesBox.getChildren().add(cv);
        }
        root.add(colonnesPane);
        creerRaccourciClavierBoutonCtrlS(buttonSaveCols, rightSide.getScene());
    }

    /*---------- METHODES FXML ----------*/

    /**
     * Sauvegarde les paramètres String et booleens
     * 
     */
    @FXML
    private void saveParams()
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

        // Sauvegarde des paramètres booleens
        for (Node node : booleanBox.getChildren())
        {
            if (node instanceof ParamBoolView)
            {
                ParamBoolView view = (ParamBoolView) node;
                mapParamsBool.put(view.getType(), view.getField().isSelected());
            }
        }

        // Enregistrement paramètres
        controlXML.saveXML(proprietesXML);
    }

    /**
     * Sauvegarde les paramètres personnels
     * 
     */
    @FXML
    private void saveParamsPerso()
    {
        // Sauvegarde des autres paramètres
        for (Node node : paramsPersoBox.getChildren())
        {
            if (node instanceof ParamView)
            {
                ParamView view = (ParamView) node;
                saveText(view.getField(), mapParamsPerso, view.getType());
            }
            else if (node instanceof ParamListView)
            {
                ParamListView view = (ParamListView) node;
                sauverListView(view.getField(), mapParamsPersoSpec, view.getType());
            }
            else if (node instanceof ParamTextView)
            {
                ParamTextView view = (ParamTextView) node;
                saveText(view.getField(), mapParamsPersoSpec, view.getType());
            }
            else
            {
                // Pas de traitement dans les autres cas.
            }
        }

        // Enregistrement paramètres
        controlXML.saveXML(propPersoXML);
    }

    @FXML
    private void saveParamsSpec()
    {
        for (Node node : paramsAutresBox.getChildren())
        {
            if (node instanceof ParamListView)
            {
                ParamListView view = (ParamListView) node;
                sauverListView(view.getField(), mapParamsSpec, view.getType());
            }
            else if (node instanceof ParamTextView)
            {
                ParamTextView view = (ParamTextView) node;
                saveText(view.getField(), mapParamsSpec, view.getType());
            }
            else
            {
                // Pas de traitement dans les autres cas.
            }
        }

        // Enregistrement paramètres
        controlXML.saveXML(proprietesXML);
    }

    @FXML
    private <T extends Enum<T> & ColR> void saveCols()
    {
        for (Node node : colonnesBox.getChildren())
        {
            if (node instanceof ColonneView)
            {
                @SuppressWarnings("unchecked")
                ColonneView<T> view = (ColonneView<T>) node;
                Map<T, String> mapCols = proprietesXML.getEnumMapColR(view.getType().getDeclaringClass());
                saveText(view.getField(), mapCols, view.getType());
            }
        }

        controlXML.saveXML(proprietesXML);
    }

    /**
     * Chargement d'un fichier Excel. Paramétrage en fonction de la méthode à utiliser pour chaque type de fichier.
     * ex: charger("Referentiel Clarity", DaoFactory.getDao(ProjetClarity.class)::recupDonneesDepuisExcel);
     * 
     * @param event
     *              Permet de récupérer la source de l'évênement pour traiter les différents fichiers.
     */
    @FXML
    private void chargerFichier(ActionEvent event)
    {
        String id = EMPTY;
        Object source = event.getSource();
        if (source instanceof Node)
            id = ((Node) source).getId();

        switch (id)
        {
            case "clarity":
                charger("Referentiel Clarity", DaoFactory.getMySQLDao(ProjetClarity.class)::recupDonneesDepuisExcel);
                break;

            case "chefSrev":
                charger("Chefs de Service", DaoFactory.getMySQLDao(ChefService.class)::recupDonneesDepuisExcel);
                break;

            case "edition":
                charger("Editions CDM", DaoFactory.getMySQLDao(Edition.class)::recupDonneesDepuisExcel);
                break;

            case "appliDir":
                charger("Applications / Direction", DaoFactory.getMySQLDao(Application.class)::recupDonneesDepuisExcel);
                break;

            default:
                break;
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    private void charger(String texte, Consumer<File> fonction)
    {
        File file = Utilities.getFileFromFileChooser("Charger " + texte);
        Platform.runLater(() -> { fonction.accept(file); alert.setContentText("Chargement " + texte + " effectue"); alert.show(); });
    }

    private <T extends TypeKey> void saveText(TextInputControl textField, Map<T, String> map, T clef)
    {
        String text = textField.getText();
        if (text != null && !text.isEmpty())
            map.put(clef, text.replace("\\", "\\\\"));
    }

    private void sauverListView(ListView<String> listView, Map<ParamSpec, String> map, ParamSpec param)
    {
        // Transformation de la liste en une chaîne de caractères, avec les valeurs separees par des points-virgules.
        StringBuilder builder = new StringBuilder();
        ObservableList<String> liste = listView.getItems();
        for (int i = 0; i < liste.size(); i++)
        {
            builder.append(liste.get(i));
            if (i < liste.size() - 1)
                builder.append(';');
        }
        map.put(param, builder.toString());
    }

    /*---------- ACCESSEURS ----------*/
}

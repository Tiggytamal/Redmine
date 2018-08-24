package view;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.enums.ParamSpec;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Classe pour afficher un paramètre de type LISTVIEW. Voir {@link model.enums.TypeParamSpec}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ParamListView extends VBox
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short VIEWMINI = 20;
    private static final short BASEPADDING = 10;
    private static final short FIELDWIDTH = 150;
    
    private ParamSpec param;
    private ListView<String> listView;
    private TextField valeurField;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamListView(ParamSpec param)
    {
        // Initialisation + style CSS
        this.param = param;
        ObservableList<Node> rootChildren = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");

        // ----- 1. Box1 -----

        HBox box = new HBox();
        box.setPadding(new Insets(BASEPADDING, BASEPADDING, BASEPADDING, BASEPADDING));

        // Label
        Label label = new Label(param.toString() + Statics.DEUXPOINTS);
        box.getChildren().add(label);

        // Region
        Region region = new Region();
        region.setPrefWidth(BASEPADDING);
        region.setPrefHeight(BASEPADDING);
        box.getChildren().add(region);

        // ListView
        listView = new ListView<>();
        String listeValeurs = Statics.proprietesXML.getMapParamsSpec().get(param);
        if (listeValeurs != null && !listeValeurs.isEmpty())
        {
            listView.getItems().clear();
            listView.getItems().addAll(listeValeurs.split(";"));
            listView.getItems().sort((o1, o2) -> o1.compareTo(o2));
        }

        // Mise à jour automatique de la hauteur de la liste des versions
        listView.getSelectionModel().selectFirst();
        double taille = (double) listView.getItems().size() * Statics.ROW_HEIGHT + 2;
        listView.setPrefHeight(taille);
        listView.setPrefWidth(calculLongueur(listView.getItems()));
        listView.getItems().addListener((ListChangeListener.Change<? extends String> c) -> listView.setPrefHeight(taille));
        listView.getItems().addListener((ListChangeListener.Change<? extends String> c) -> listView.setPrefWidth(calculLongueur(listView.getItems())));
        box.getChildren().add(listView);

        // Region
        region = new Region();
        region.setPrefWidth(BASEPADDING);
        box.getChildren().add(region);

        // Bouton supprimer
        Button suppr = new Button("supprimer");
        suppr.setOnAction(event -> supprimer());
        box.getChildren().add(suppr);

        rootChildren.add(box);

        region = new Region();
        region.setPrefWidth(BASEPADDING);
        region.setPrefHeight(BASEPADDING);
        rootChildren.add(region);

        // ----- 2. Box2 -----
        box = new HBox();

        // TextField
        valeurField = new TextField();
        valeurField.setPrefWidth(FIELDWIDTH);
        box.getChildren().add(valeurField);

        // Bouton ajouter
        Button add = new Button("ajouter");
        add.setOnAction(event -> ajouter());
        box.getChildren().add(add);

        // Region
        region = new Region();
        region.setPrefWidth(BASEPADDING);
        region.setPrefHeight(BASEPADDING);
        box.getChildren().add(region);

        rootChildren.add(box);

        // ----- 3. Séparateur -----
        Separator separ = new Separator();
        separ.setPadding(new Insets(BASEPADDING, BASEPADDING/2, BASEPADDING, BASEPADDING/2));
        rootChildren.add(separ);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Sauvegarde la liste des valeurs dans la map des propriétés
     */
    public void sauverValeurs()
    {
        // Transformation de la liste en une chaîne de caractères, avec les valeurs séparèes par des points-virgules.
        StringBuilder builder = new StringBuilder();
        ObservableList<String> liste = listView.getItems();
        for (int i = 0; i < liste.size(); i++)
        {
            builder.append(liste.get(i));
            if (i < liste.size() - 1)
                builder.append(";");
        }
        Statics.proprietesXML.getMapParamsSpec().put(param, builder.toString());
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Retire une responable de la liste
     */
    private void supprimer()
    {
        // Suppression du responsable de la liste affichée
        int index = listView.getSelectionModel().getSelectedIndex();
        ObservableList<String> liste = listView.getItems();
        if (index != -1)
        {
            liste.remove(index);
            if (!liste.isEmpty())
                listView.getSelectionModel().select(index - 1);
        }
        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    /**
     * Ajoute un responsable à la liste
     */
    private void ajouter()
    {
        String valeur = valeurField.getText();
        ObservableList<String> liste = listView.getItems();

        if (liste.contains(valeur))
            throw new FunctionalException(Severity.ERROR, "La valeur est déjà contenu dans la liste.");

        // Controôle de la valeur rentrée et ajout à la liste
        ajouterValeur(valeur, liste);

        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    /**
     * Contrôle de la valeur rentrée par l'utilisateur et ajout à la liste.
     * 
     * @param valeur
     * @param liste
     * @return
     */
    private void ajouterValeur(String valeur, ObservableList<String> liste)
    {

        switch (param.getType())
        {
            case LISTVIEWNOM:
                try
                {
                    if (ControlRTC.INSTANCE.recupContributorDepuisNom(valeur) == null)
                        throw new FunctionalException(Severity.ERROR, "Le nom n'est pas reconnu dans RTC. Essayez [NOM] [Prenom] sans accent.");
                }
                catch (TeamRepositoryException e)
                {
                    throw new TechnicalException("Erreur appel RTC depuis viw.ParamListView.testNom - " + valeur, e);
                }
                break;

            case LISTVIEWVERSION:
                if (!valeur.matches("^E[0-9][0-9]"))
                    throw new FunctionalException(Severity.ERROR, "L'édition doit être de la forme ^E[0-9][0-9]");
                break;

            case LISTVIEWCOMPO:
                if (!valeur.matches("^[0-9][0-9]"))
                    throw new FunctionalException(Severity.ERROR, "La version doit être de la forme ^[0-9][0-9]");
                break;

            default:
                throw new IllegalArgumentException("Méthode view.ParamListView.ajouterValeur - le type de TypeParamSpec n'est pas géré : " + param.getType());
        }

        // Ajout de la valeur à la liste
        liste.add(valeur);
    }

    /**
     * Calcul la longueur de la fenêtre pour la listView
     * 
     * @param items
     * @return
     */
    private double calculLongueur(ObservableList<String> items)
    {
        double retour = 0;
        for (String string : items)
        {
            double temp = string.length() * Statics.CARAC_WIDTH;
            if (temp > retour)
                retour = temp;
        }
        return retour + VIEWMINI;
    }

    /*---------- ACCESSEURS ----------*/
}

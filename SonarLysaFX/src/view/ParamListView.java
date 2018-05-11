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
import model.enums.TypeParamSpec;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class ParamListView extends VBox
{
    /*---------- ATTRIBUTS ----------*/

    private TypeParamSpec typeParam;
    private ListView<String> listView;
    private TextField newResp;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ParamListView(TypeParamSpec typeParam)
    {
        this.typeParam = typeParam;
        ObservableList<Node> rootChildren = getChildren();

        // ----- 1. Box1 -----

        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));

        // Label
        Label label = new Label(typeParam.toString() + Statics.DEUXPOINTS);
        label.setPrefWidth(200);
        box.getChildren().add(label);

        // Region
        Region region = new Region();
        region.setPrefWidth(10);
        region.setPrefHeight(10);
        box.getChildren().add(region);

        // ListView
        listView = new ListView<>();
        String listeValeurs = Statics.proprietesXML.getMapParamsSpec().get(typeParam);
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
        listView.getItems().addListener((ListChangeListener.Change<? extends String> c) -> listView.setPrefHeight(taille));
        box.getChildren().add(listView);

        // Region
        region = new Region();
        region.setPrefWidth(10);
        region.setPrefHeight(10);
        box.getChildren().add(region);

        // Bouton supprimer
        Button suppr = new Button("supprimer");
        suppr.setOnAction(event -> supprimer());
        box.getChildren().add(suppr);

        rootChildren.add(box);
        
        region = new Region();
        region.setPrefWidth(10);
        region.setPrefHeight(10);
        rootChildren.add(region);

        // ----- 2. Box2 -----
        box = new HBox();

        // TextField
        newResp = new TextField();
        newResp.setPrefWidth(150);
        box.getChildren().add(newResp);

        // Bouton ajouter
        Button add = new Button("ajouter");
        suppr.setOnAction(event -> ajouter());
        box.getChildren().add(add);

        // Region
        region = new Region();
        region.setPrefWidth(10);
        region.setPrefHeight(10);
        box.getChildren().add(region);
        
        rootChildren.add(box);

        // ----- 3. Séparateur -----
        Separator separ = new Separator();
        separ.setPadding(new Insets(10, 5, 10, 5));
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
        Statics.proprietesXML.getMapParamsSpec().put(typeParam, builder.toString());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

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
        String resp = newResp.getText();
        ObservableList<String> liste = listView.getItems();

        // On contrôle la bonne structure du nom de la version et on ne crée pas de doublon
        if (testNom(resp) && !liste.contains(resp))
            liste.add(resp);
        else
            throw new FunctionalException(Severity.ERROR, "Le nom n'est pas reconnu dans RTC. Essayez [NOM] [Prenom] sans accent.");
        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    /**
     * Contrôle que le nom du responsable est bien présent dans RTC.
     * @param nom
     * @return
     */
    private boolean testNom(String nom)
    {
        try
        {
            return ControlRTC.INSTANCE.recupContributorDepuisNom(nom) != null;
        } catch (TeamRepositoryException e)
        {
            Statics.logger.error("Erreur appel RTC depuis viw.ParamListView.testNom - " + nom);
            return false;
        }
    }
}

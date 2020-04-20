package fxml.node;

import java.util.List;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.enums.ParamSpec;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe pour afficher un paramètre de type LISTVIEW. Voir {@link model.enums.ParamSpec}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ParamListView extends AbstractBaseView<ParamSpec, ListView<String>>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short VIEWMINI = 20;

    @FXML
    private Label label;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField valeurField;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamListView(ParamSpec param)
    {
        super("ParamList.fxml", param);

        // Initialisation textes
        String listeValeurs;

        // Label
        label.setText(param.getNom() + Statics.DEUXPOINTS);

        // ListView - Récupération de la valeur dans le bon fichier selon si le paramètre est personnel ou non.
        if (param.isPerso())
            listeValeurs = Statics.propPersoXML.getParamsSpec().get(param);
        else
            listeValeurs = Statics.proprietesXML.getMapParamsSpec().get(param);

        // Initialisation
        if (listeValeurs != null && !listeValeurs.isEmpty())
        {
            listView.getItems().clear();
            listView.getItems().addAll(listeValeurs.split(";"));
            listView.getItems().sort((o1, o2) -> o1.compareTo(o2));
        }

        // Mise à jour automatique de la hauteur de la liste des versions
        listView.getSelectionModel().selectFirst();
        listView.setPrefHeight(calculHauteur(listView.getItems()));
        listView.setPrefWidth(calculLongueur(listView.getItems()));
        listView.getItems().addListener(
                (ListChangeListener.Change<? extends String> c) -> { 
                    listView.setPrefHeight(calculHauteur(listView.getItems())); 
                    listView.setPrefWidth(calculLongueur(listView.getItems())); 
                    });
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Sauvegarde la liste des valeurs dans la map des propriétés
     * 
     * @param map
     *            Map des données à sauvegarder.
     */
    public void sauverValeurs(Map<ParamSpec, String> map)
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

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Retire une responsable de la liste
     */
    @FXML
    private void supprimer()
    {
        // Suppression du responsable de la liste affichee
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
    @FXML
    private void ajouter()
    {
        String valeur = valeurField.getText();
        ObservableList<String> liste = listView.getItems();

        if (liste.contains(valeur))
            throw new FunctionalException(Severity.INFO, "La valeur est déjà contenu dans la liste.");

        // Contrôle de la valeur rentrée et ajout à la liste

        switch (param.getType())
        {
            case LISTVIEWNOM:
                try
                {
                    if (ControlRTC.getInstance().recupContributorDepuisNom(valeur) == null)
                        throw new FunctionalException(Severity.INFO, "Le nom n'est pas reconnu dans RTC. Essayez [NOM] [Prenom] sans accent.");
                }
                catch (TeamRepositoryException e)
                {
                    throw new TechnicalException("Méthode fxml.view.ParamListView.ajouter - Erreur appel RTC " + valeur, e);
                }
                break;

            default:
                throw new TechnicalException("Méthode fxml.view.ParamListView.ajouter - le type de ParamSpec n'est pas géré : " + param.getType());
        }

        // Ajout de la valeur à la liste et tri de celle-ci
        liste.add(valeur);
        liste.sort((o1, o2) -> o1.compareTo(o2));
    }

    /**
     * Calcul la longueur de la fenêtre pour la listView
     * 
     * @param items
     *              Liste des objest de la fenêtre
     * @return
     *         Longueur de la fenêtre.
     */
    private double calculLongueur(List<String> items)
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

    /**
     * Calcul la hauteur de la fenêtre pour la listView
     * 
     * @param items
     *              Liste des objest de la fenêtre
     * @return
     *         Longueur de la fenêtre.
     */
    private double calculHauteur(List<String> items)
    {
        double retour = 0;
        if (items != null)
            retour = (double) items.size() * Statics.ROW_HEIGHT + 2;
        return retour;
    }

    /*---------- ACCESSEURS ----------*/

    @Override
    public ListView<String> getField()
    {
        return listView;
    }
}

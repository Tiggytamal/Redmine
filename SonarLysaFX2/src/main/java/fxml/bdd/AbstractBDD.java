package fxml.bdd;

import static utilities.Statics.EMPTY;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import dao.Dao;
import fxml.factory.TableCellFXMLFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.bdd.AbstractBDDModele;
import model.enums.Action;
import model.fxml.AbstractFXMLModele;
import model.fxml.FiltreurFXML;
import model.fxml.ListeGetters;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe mère abstraite des contrôleurs des tableaux de la base de données.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public abstract class AbstractBDD<T extends AbstractFXMLModele<I>, A extends Action, I>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    private static final double FACTOR = 10D;
    private static final int MINSIZE = 40;

    @FXML
    protected TableView<T> table;

    @FXML
    protected ComboBox<A> comboAction;

    @FXML
    protected Label total;

    protected FilteredList<T> filteredList;
    protected CheckBox selectionTotal;
    protected String titreExtract;

    private T objet;
    private ComboBox<ListeGetters> comboColonne;

    /*---------- CONSTRUCTEURS ----------*/

    public AbstractBDD()
    {
        titreExtract = Statics.EMPTY;

        // Initialisation d'un objet vide pour permettre d'accéder au l'énumération de la liste des getters
        initModele();
    }

    @FXML
    public void initialize()
    {
        // 1. Initilisation des colonnes de la ListView. Pour cela on utilise l'énumération du modele
        Map<String, TableColumn<T, TableColumn<T, Object>>> listePeres = new HashMap<>();

        // On utilise l'interface générique pour créer les colonnes
        for (ListeGetters getter : objet.getListeGetters())
        {
            // Création de la colonne groupe
            TableColumn<T, TableColumn<T, Object>> pere = listePeres.get(getter.getGroupe());
            if (pere == null)
            {
                pere = new TableColumn<>(getter.getGroupe());
                pere.getStyleClass().add(getter.getStyle());
                table.getColumns().add(pere);
                listePeres.put(getter.getGroupe(), pere);
            }

            // Traitement de la colonne d'un paramètre et ajout à la colonne du groupe
            TableColumn<T, Object> fils = new TableColumn<>(getter.getAffichage());

            // Utilisation de la factory pour la création des cellules et permettre les hyperlink
            fils.setCellFactory(new TableCellFXMLFactory<>());

            double size = getter.getAffichage().length() * FACTOR + MINSIZE;
            if (size > fils.getPrefWidth())
                fils.setPrefWidth(size);

            // Liens de chaque cellule au paramètre du modele correspondant
            fils.setCellValueFactory(new PropertyValueFactory<T, Object>(getter.getNomParam()));

            // Gestion affichage par defaut
            fils.setVisible(getter.isAffParDefaut());

            // Initialisation du style depuis le css
            fils.getStyleClass().add(getter.getStyle());
            pere.getColumns().add(fils);
            table.getSortOrder().add(fils);
        }

        // Ajout d'une fonction de rafraichissement au moment de trier la table.
        table.onSortProperty().set(event -> table.refresh());

        initializeImpl();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Implementation specifique de l'intialisation de la vue
     */
    protected abstract void initializeImpl();

    /**
     * Implementation specifique du rafraichiseement des données du tableau
     */
    protected abstract void refreshListImpl();

    /**
     * Implémentation spécifique de l'extraction des données du tableau
     */
    protected abstract void extractImpl();

    /**
     * Implementaion des actions à la validation d'une action
     */
    protected abstract void valider();

    /*---------- METHODES PUBLIQUES ----------*/

    protected <O extends AbstractBDDModele<I>> List<O> listeObjetsATraiter(Dao<O, I> dao)
    {
        // Récupération de la liste des composants
        List<O> objets = new ArrayList<>();

        for (T cFXML : filteredList)
        {
            if (cFXML.getSelected().isSelected())
                objets.add(dao.recupEltParIndex(cFXML.getIndex()));
        }

        return objets;
    }

    protected A getAction()
    {
        return comboAction.getSelectionModel().getSelectedItem();
    }

    /**
     * Rafraichit la liste des données sans conservation du predicat de filtre
     */
    public void refreshList()
    {
        refreshList(null);
    }

    /**
     * Rafraichit la liste des données en utilisant le predicat de filtre s'il est present
     * 
     * @param predicate
     *                  Prédicat pour le filtre.
     */
    public void refreshList(FiltreurFXML<T> predicate)
    {
        refreshListImpl();

        if (predicate != null)
            filteredList.setPredicate(predicate);

        selectionTotal.selectedProperty().addListener((observable, oldValue, newValue) -> filteredList.stream().forEach(dq -> dq.getSelected().setSelected(newValue)));

        // Création d'une nouvelle SortedList depuis la filteredList et ajout à la table
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
        total.setText(String.valueOf(sortedList.size()));
        table.refresh();
    }

    /**
     * Création d'un nouveau sur la liste affichee
     * 
     * @param filtre
     *               Valeur du filtre à utiliser.
     */
    @SuppressWarnings("unchecked")
    public void filtrer(String filtre)
    {
        // Contrôle si les infos n'ont pas été entrées
        if (filtre == null || filtre.isEmpty() || comboColonne.getSelectionModel().getSelectedItem() == null)
            throw new TechnicalException("control.view.fxml.AbstractFXMLViewControl.filtrer - le filtre est null ou la comboBox n'a pas de sélection.");

        filteredList.setPredicate(new FiltreurFXML<T>(filtre, comboColonne.getSelectionModel().getSelectedItem().getNomMethode(), (t, u) -> {
            try
            {
                Object test = objet.getClass().getMethod(u).invoke(t);
                if (test instanceof String)
                    return (String) test;
                else if (test instanceof List)
                    return ((List<String>) test).get(0);
                return String.valueOf(test);
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
            {
                LOGPLANTAGE.error(EMPTY, e);
            }
            return null;
        }, (FiltreurFXML<T>) filteredList.getPredicate()));
    }

    /**
     * Suppression de tous les filtres du tableau
     */
    public void supprimerFiltre()
    {
        filteredList.setPredicate(null);
    }

    /**
     * Initialisation de la liste des colonnes possibles pour chaque tableau pour choisir les filtres
     * 
     * @param comboColonne
     *                     Node à utiliser.
     */
    public void initComboFiltre(ComboBox<ListeGetters> comboColonne)
    {
        this.comboColonne = comboColonne;
        comboColonne.getItems().clear();
        for (ListeGetters getter : objet.getListeGetters())
        {
            // Protection pour ne pas rajouter la colonne correspondante à la checkbox de sélection
            if (!getter.getAffichage().isEmpty())
                comboColonne.getItems().add(getter);
        }
    }

    /*---------- METHODES PROTECTED ----------*/
    /*---------- METHODES PRIVEES ----------*/

    /**
     * Intialisation de la classe du modèle en utilisant l'introspection.
     */
    @SuppressWarnings("unchecked")
    private void initModele()
    {
        // Permet de récupérer la classe sous forme de type parametre
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String classNameObjet = Statics.PATTERNSPACE.split(pt.getActualTypeArguments()[0].toString())[1];

        // Instantiate the Parameter and initialize it.
        try
        {
            Class<T> classObjet = (Class<T>) Class.forName(classNameObjet);
            objet = classObjet.newInstance();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("Impossible d'instancier le modele FXML - control.view.fxml.AbstractFXMLViewControl", e);
        }
    }

    /*---------- ACCESSEURS ----------*/

    public final TableView<T> getTable()
    {
        return table;
    }

    @SuppressWarnings("unchecked")
    public FiltreurFXML<T> getPredicate()
    {
        return (FiltreurFXML<T>) filteredList.getPredicate();
    }

    public void setSelectionTotal(CheckBox selectionTotal)
    {
        this.selectionTotal = selectionTotal;
    }

    public CheckBox getSelectionTotal()
    {
        return selectionTotal;
    }

    public String getTitreExtraction()
    {
        return titreExtract;
    }
}

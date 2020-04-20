package fxml.bdd;

import java.util.ArrayList;
import java.util.List;

import control.task.LaunchTask;
import control.task.action.TraiterActionDefautQualiteTask;
import control.task.maj.RelancerAnomaliesTask;
import control.task.maj.TraiterDefautMCTask;
import dao.Dao;
import dao.DaoFactory;
import fxml.dialog.AjouterDefautDialog;
import fxml.factory.DefautQualiteFXMLTableRowFactory;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import model.bdd.AnomalieRTC;
import model.bdd.DefautQualite;
import model.enums.ActionDq;
import model.fxml.DefautQualiteFXML;
import utilities.Statics;

/**
 * Controleur de l'affichage des DefautQualite.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class DefautQualiteBDD extends AbstractBDD<DefautQualiteFXML, ActionDq, String>
{
    /*---------- ATTRIBUTS ----------*/

    private Dao<DefautQualite, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public DefautQualiteBDD()
    {
        dao = DaoFactory.getMySQLDao(DefautQualite.class);
        titreExtract = "Défauts Qualité";
    }

    @Override
    public void initializeImpl()
    {
        table.setRowFactory(new DefautQualiteFXMLTableRowFactory(this, dao));
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected void refreshListImpl()
    {
        // Initialisation de la liste des défauts à reonter dans le tableau.
        List<DefautQualiteFXML> listeFXML = new ArrayList<>();

        // On récupère tous les défauts non clos et on les ajoute à la liste.
        for (DefautQualite defautQualite : dao.readAll())
        {
            // Ajout à la liste des défauts qui ne sont pas abandonnées et pas clos ou clos mais avec un qualityGate en erreur et liaison de la selection à la
            // selection générale
            if (defautQualite.calculDefautEnCours())
            {
                DefautQualiteFXML dq = DefautQualiteFXML.build(defautQualite);
                listeFXML.add(dq);
            }
        }

        // Création dune nouvelle liste filtree, sauvegarde et remise du filtre pour ne pas le perdre entre deux actions
        filteredList = new FilteredList<>(FXCollections.observableArrayList(listeFXML));
    }

    @Override
    protected void extractImpl()
    {
        // Rajout des données du tableau des anomalies RTC à l'extraction
        // Pour cela on va rajouter à la liste du tableau les données en base transformées en DefautQualiteFXML
        // Comme cela, pas besoin de traitement sur le fichier Excel.

        // 1. Création d'une nouvelle fileteredList avec récupération des AnomalieRTC en base de données
        List<AnomalieRTC> liste = DaoFactory.getMySQLDao(AnomalieRTC.class).readAll();

        // Création de la nouvelle liste avec une taille initiale de 1.25x la liste initiale plus la liste des anomaliesRTC en base.
        List<DefautQualiteFXML> newListe = new ArrayList<>((int) ((filteredList.getSource().size() + liste.size()) * Statics.RATIOLOAD));

        // Récupération de la liste existante
        newListe.addAll(filteredList.getSource());

        // Ajout des données issues de la base
        for (AnomalieRTC ano : liste)
        {
            newListe.add(ano.buildDefautQualiteFXML());
        }

        // Création de la nouvelle filetredList
        filteredList = new FilteredList<>(FXCollections.observableArrayList(newListe));

        // Création d'une nouvelle SortedList depuis la filteredList et ajout à la table
        SortedList<DefautQualiteFXML> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    @FXML
    @Override
    protected void valider()
    {
        LaunchTask.startTaskWithAction(new TraiterActionDefautQualiteTask(getAction(), listeObjetsATraiter(dao)), t -> refreshList(getPredicate()), this);
    }

    @FXML
    protected void relancer()
    {
        LaunchTask.startTaskWithAction(new RelancerAnomaliesTask(), t -> refreshList(getPredicate()), this);
    }

    @FXML
    protected void majMobileCenter()
    {
        LaunchTask.startTaskWithAction(new TraiterDefautMCTask(), t -> refreshList(getPredicate()), this);
    }

    @FXML
    protected void creerDefaut()
    {
        // OUverture d'une fenêtre de création d'un nouveau défaut qualite. Et enregistrement de celui-ci en base
        AjouterDefautDialog dialog = new AjouterDefautDialog();
        dialog.showAndWait().ifPresent(dq -> { DaoFactory.getMySQLDao(DefautQualite.class).persist(dq); refreshList(getPredicate()); });
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package fxml.bdd;

import java.util.ArrayList;
import java.util.List;

import control.task.LaunchTask;
import control.task.action.TraiterActionAnomalieRTCTask;
import dao.Dao;
import dao.DaoFactory;
import fxml.dialog.AjouterAnoDialog;
import fxml.factory.AnomalieRTCFXMLTableRowFactory;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import model.bdd.AnomalieRTC;
import model.enums.ActionA;
import model.fxml.AnomalieRTCFXML;

/**
 * Controleur de l'affichage des anomalies RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class AnomalieRTCBDD extends AbstractBDD<AnomalieRTCFXML, ActionA, Integer>
{
    /*---------- ATTRIBUTS ----------*/

    private Dao<AnomalieRTC, Integer> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public AnomalieRTCBDD()
    {
        dao = DaoFactory.getMySQLDao(AnomalieRTC.class);
        titreExtract = "Anomalies RTC";
    }

    @Override
    public void initializeImpl()
    {
        table.setRowFactory(new AnomalieRTCFXMLTableRowFactory(this, dao));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void refreshListImpl()
    {
        // Initialisation de la liste des anomalies à remonter dans le tableau.
        List<AnomalieRTCFXML> listeFXML = new ArrayList<>(100);

        // On récupère tous les anomalies et on les ajoute à la liste.
        for (AnomalieRTC ano : dao.readAll())
        {
            AnomalieRTCFXML dq = AnomalieRTCFXML.build(ano);
            listeFXML.add(dq);
        }

        // Création dune nouvelle liste filtree, sauvegarde et remise du filtre pour ne pas le perdre entre deux actions
        filteredList = new FilteredList<>(FXCollections.observableArrayList(listeFXML));
    }

    @Override
    protected void extractImpl()
    {
        // Pas d'implémentation spécifique pour l'extraction
    }

    @FXML
    @Override
    protected void valider()
    {
        LaunchTask.startTaskWithAction(new TraiterActionAnomalieRTCTask(getAction(), listeObjetsATraiter(dao)), t -> refreshList(getPredicate()), this);
    }

    @FXML
    protected void ajouterAno()
    {
        // OUverture d'une fenêtre de création d'un nouveau défaut qualite. Et enregistrement de celui-ci en base
        AjouterAnoDialog dialog = new AjouterAnoDialog();
        dialog.showAndWait().ifPresent(ano -> { dao.persist(ano); refreshList(getPredicate()); });
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

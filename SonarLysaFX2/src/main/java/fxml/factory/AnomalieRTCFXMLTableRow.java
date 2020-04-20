package fxml.factory;

import java.util.Optional;

import control.task.LaunchTask;
import control.task.action.TraiterActionAnomalieRTCTask;
import dao.Dao;
import fxml.bdd.AnomalieRTCBDD;
import fxml.dialog.AjouterAnoDialog;
import javafx.scene.control.MenuItem;
import model.bdd.AnomalieRTC;
import model.enums.ActionA;
import model.fxml.AnomalieRTCFXML;

public class AnomalieRTCFXMLTableRow extends FXMLTableRow<AnomalieRTCFXML, Integer, ActionA, Dao<AnomalieRTC, Integer>, AnomalieRTC, AnomalieRTCBDD>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des actions possibles
    private MenuItem modifItem;
    private MenuItem supprItem;

    /*---------- CONSTRUCTEURS ----------*/

    public AnomalieRTCFXMLTableRow(AnomalieRTCBDD control, Dao<AnomalieRTC, Integer> dao)
    {
        super(dao, control, ActionA.values());
        
        
        
        modifItem = new MenuItem("Modifier");
        modifItem.setOnAction(event -> {

            // Récupération du defaut en base de donnees
            AnomalieRTC ano = dao.recupEltParIndex(getItem().getIndex());

            // Création de la fenêtre pour modifier l'anomalie et sauvegarder les modifications
            Optional<AnomalieRTC> result = new AjouterAnoDialog(ano).showAndWait();
            result.ifPresent(s -> dao.persist(ano));
            control.refreshList(control.getPredicate());
        });

        supprItem = new MenuItem("Supprimer");
        supprItem.setOnAction(event -> LaunchTask.startTaskWithAction(new TraiterActionAnomalieRTCTask(ActionA.SUPPRIMER, dao.recupEltParIndex(getItem().getIndex())),
                t -> control.refreshList(control.getPredicate()), control));

        // Création du menu contextuel et ajout des boutons
        rowMenu.getItems().addAll(modifItem, supprItem);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

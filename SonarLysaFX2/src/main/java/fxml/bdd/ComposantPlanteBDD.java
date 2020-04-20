package fxml.bdd;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import control.rest.SonarAPI;
import control.task.LaunchTask;
import control.task.action.TraiterActionCompoPlanteTask;
import dao.Dao;
import dao.DaoFactory;
import fxml.factory.ComposantPlanteFXMLTableRowFactory;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.ModelFactory;
import model.bdd.ComposantErreur;
import model.enums.ActionCp;
import model.enums.TypeObjetSonar;
import model.fxml.ComposantPlanteFXML;
import model.rest.sonarapi.ComposantSonar;
import utilities.Statics;

/**
 * Controleur de l'affichage des composants plantés dans SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ComposantPlanteBDD extends AbstractBDD<ComposantPlanteFXML, ActionCp, String>
{
    /*---------- ATTRIBUTS ----------*/

    private Dao<ComposantErreur, String> dao;

    @FXML
    private Button analyse;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantPlanteBDD()
    {
        dao = DaoFactory.getMySQLDao(ComposantErreur.class);
        titreExtract = "Composants SonarQube en erreur";
    }

    @Override
    protected void initializeImpl()
    {
        table.setRowFactory(new ComposantPlanteFXMLTableRowFactory(this, dao));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void refreshListImpl()
    {
        // Initialisation de la liste des défauts à rmeonter dans le tableau.
        List<ComposantErreur> compos = dao.readAll();
        List<ComposantPlanteFXML> listeFXML = new ArrayList<>((int) (compos.size() * Statics.RATIOLOAD));

        // On récupère tous les composants en erreur et les ajoute à la liste
        for (ComposantErreur ce : compos)
        {
            ComposantPlanteFXML dq = ComposantPlanteFXML.build(ce);
            listeFXML.add(dq);
        }

        // Création dune nouvelle liste filtree, sauvegarde et remise du filtre pour ne pas le perdre entre deux actions
        filteredList = new FilteredList<>(FXCollections.observableArrayList(listeFXML));
        selectionTotal.selectedProperty().addListener((observable, oldValue, newValue) -> filteredList.stream().forEach(dq -> dq.getSelected().setSelected(newValue)));
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
        LaunchTask.startTaskWithAction(new TraiterActionCompoPlanteTask(getAction(), listeObjetsATraiter(dao)), t -> refreshList(getPredicate()), this);
    }

    @FXML
    protected void analyse()
    {
        // Récupération des composants plantes en table
        Map<String, ComposantErreur> compoEnErreur = dao.readAllMap();

        SonarAPI sonarAPI = SonarAPI.build();

        // Remise à false de tous les top de purge
        for (ComposantErreur erreur : compoEnErreur.values())
        {
            erreur.setExiste(!sonarAPI.getObjetSonarParNomOuType(erreur.getKey(), TypeObjetSonar.PROJECT).isEmpty());
            erreur.setaPurger(false);
        }

        for (ComposantSonar compo : sonarAPI.getComposPlantes(null))
        {
            // Si le composant existe, on remet le top de purge à oui
            if (compoEnErreur.containsKey(compo.getKey()))
                compoEnErreur.get(compo.getKey()).setaPurger(true);

            // Sinon, on rajoute une ligne dans la base de donnes
            else
            {
                ComposantErreur erreur = ModelFactory.build(ComposantErreur.class);
                erreur.setaPurger(true);
                erreur.setDateDetection(LocalDate.now());
                erreur.setKey(compo.getKey());
                erreur.setNom(compo.getNom());
                compoEnErreur.put(erreur.getKey(), erreur);
            }
        }

        // Mise à jour base de données et affichage
        dao.persist(compoEnErreur.values());
        refreshList(getPredicate());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

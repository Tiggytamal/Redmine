package fxml.bdd;

import java.util.ArrayList;
import java.util.List;

import control.task.LaunchTask;
import control.task.action.TraiterActionUtilisateurTask;
import dao.Dao;
import dao.DaoFactory;
import fxml.factory.UtilisateurFXMLTableRowFactory;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import model.bdd.Utilisateur;
import model.enums.ActionU;
import model.fxml.UtilisateurFXML;
import utilities.Statics;

/**
 * Controleur de l'affichage des utilisateurs RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class UtilisateurBDD extends AbstractBDD<UtilisateurFXML, ActionU, String>
{
    /*---------- ATTRIBUTS ----------*/

    private Dao<Utilisateur, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public UtilisateurBDD()
    {
        dao = DaoFactory.getMySQLDao(Utilisateur.class);
        titreExtract = "Utilisateurs RTC";
    }

    @Override
    public void initializeImpl()
    {
        table.setRowFactory(new UtilisateurFXMLTableRowFactory(this, dao));
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected void refreshListImpl()
    {
        // Initialisation de la liste des composants à remonter dans le tableau.
        List<Utilisateur> compos = dao.readAll();
        List<UtilisateurFXML> listeFXML = new ArrayList<>((int) (compos.size() * Statics.RATIOLOAD));

        // On récupère tous les composants et on les ajoute à la liste d'affichage
        for (Utilisateur compo : compos)
        {
            listeFXML.add(UtilisateurFXML.build(compo));
        }

        // On lit cette liste à la propriété du modéle, puis on lit la propriété des défauts affichables aprés filtrage à la ListView.
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
        LaunchTask.startTaskWithAction(new TraiterActionUtilisateurTask(getAction(), listeObjetsATraiter(dao)), t -> refreshList(getPredicate()), this);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

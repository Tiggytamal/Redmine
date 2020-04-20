package fxml.bdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import control.task.LaunchTask;
import control.task.action.TraiterActionCompoTask;
import dao.Dao;
import dao.DaoFactory;
import fxml.factory.ComposantFXMLTableRowFactory;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import model.bdd.ComposantBase;
import model.enums.ActionC;
import model.fxml.ComposantFXML;
import utilities.Statics;

/**
 * Controleur de l'affichage des composants dans SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class ComposantBDD extends AbstractBDD<ComposantFXML, ActionC, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final Pattern PATTERNNUM = Pattern.compile("^.*\\d\\d$");
    private static final Pattern PATTERNNAMECOMPO = Pattern.compile("\\s\\d{2}$");

    private Dao<ComposantBase, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantBDD()
    {
        dao = DaoFactory.getMySQLDao(ComposantBase.class);
        titreExtract = "Composants SonarQube";
    }

    @Override
    protected void initializeImpl()
    {
        table.setRowFactory(new ComposantFXMLTableRowFactory(this, dao));
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected void refreshListImpl()
    {
        // Initialisation de la liste des composants à remonter dans le tableau.
        List<ComposantBase> compos = dao.readAll();
        List<ComposantFXML> listeFXML = new ArrayList<>((int) (compos.size() * Statics.RATIOLOAD));

        // On récupère tous les composants et on les ajoute à la liste d'affichage
        for (ComposantBase compo : compos)
        {
            listeFXML.add(ComposantFXML.build(compo));
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
        LaunchTask.startTaskWithAction(new TraiterActionCompoTask(getAction(), listeObjetsATraiter(dao)), t -> refreshList(getPredicate()), this);
    }

    @FXML
    private void analyse()
    {
        List<ComposantBase> compos = dao.readAll();

        // tri des composants par numérotation
        compos.sort((o1, o2) -> {
            if (!PATTERNNUM.matcher(o1.getNom()).matches() && !PATTERNNUM.matcher(o2.getNom()).matches())
                return 0;
            if (!PATTERNNUM.matcher(o1.getNom()).matches())
                return -1;
            if (!PATTERNNUM.matcher(o2.getNom()).matches())
                return 1;
            return Integer.valueOf(o2.getNom().substring(o2.getNom().length() - 2, o2.getNom().length()))
                    .compareTo(Integer.valueOf(o1.getNom().substring(o1.getNom().length() - 2, o1.getNom().length())));
        });

        // Map de tri des composants
        Map<String, List<ComposantBase>> retour = new HashMap<>();

        for (ComposantBase compo : compos)
        {
            // On ne prend pas en compte les branches non master dans le traitement
            if (!"master".equals(compo.getBranche()))
                continue;

            String nom = compo.getNom();
            String key;

            // Réinitialisation de la valeur doublon à faux
            compo.setDoublon(false);

            // SI la nom du composant finit par [ XX] on enlève les 3 dérniers acatères.
            if (PATTERNNAMECOMPO.matcher(nom).find())
                key = nom.substring(0, nom.length() - 3);
            else
                key = nom;

            retour.computeIfAbsent(key, l -> new ArrayList<>());
            List<ComposantBase> liste = retour.get(key);
            if (!liste.isEmpty())
            {
                compo.setDoublon(true);
                dao.persist(compo);
            }
            liste.add(compo);

        }

        refreshList(getPredicate());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

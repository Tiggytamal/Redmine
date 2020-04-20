package fxml.factory;

import java.util.List;
import java.util.function.Function;

import control.task.LaunchTask;
import control.task.action.AbstractTraiterActionTask;
import dao.Dao;
import fxml.bdd.AbstractBDD;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.bdd.AbstractBDDModele;
import model.enums.Action;
import model.fxml.AbstractFXMLModele;

public class FXMLTableRow<T extends AbstractFXMLModele<I>, I, A extends Action, D extends Dao<M, I>, M extends AbstractBDDModele<I>, C extends AbstractBDD<T, A, I>> extends TableRow<T>
{
    /*---------- ATTRIBUTS ----------*/

    protected ContextMenu rowMenu;
    protected D dao;
    protected C control;
    protected A[] actions;

    /*---------- CONSTRUCTEURS ----------*/

    @SuppressWarnings("unchecked")
    public FXMLTableRow(D dao, C control, A[] actions)
    {
        // Intialisation variables
        this.dao = dao;
        this.control = control;
        this.actions = actions;

        // Création option de copie d'une cellule
        MenuItem copyItem = new MenuItem("Copier");
        copyItem.setOnAction(event -> {
            // Récupération de la cellule selectionnee
            Object value = getTableView().getSelectionModel().getSelectedCells().get(0).getTableColumn().getCellObservableValue(getItem()).getValue();

            // Création du clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();

            // Ajout de la valeur de la cellule dans le clipboard
            if (value instanceof String)
                content.putString((String) value);
            else if (value instanceof ObservableList)
                content.putString(((List<String>) value).get(0));
            else
                content.putString(value.toString());

            clipboard.setContent(content);
        });

        // Création du menu contextuel et ajout du bouton
        rowMenu = new ContextMenu();
        rowMenu.getItems().add(copyItem);

        // Liaison du menu à la ligne en cours seulement si celle-ci n'est pas nulle.
        contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(itemProperty())).then(rowMenu).otherwise((ContextMenu) null));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void updateItem(T item, boolean empty)
    {
        super.updateItem(item, empty);

        if (item == null || empty)
        {
            setStyle(null);
        }
    }

    protected void creerMenu(Function<A, AbstractTraiterActionTask<M, A, D, I>> getTask)
    {
        for (A action : actions)
        {
            MenuItem item = new MenuItem(action.getValeur());
            item.setOnAction(event -> LaunchTask.startTaskWithAction(getTask.apply(action), t -> control.refreshList(control.getPredicate()), control));
            rowMenu.getItems().add(item);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

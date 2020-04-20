package fxml.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import model.fxml.AbstractFXMLModele;

/**
 * Factory pour créer les cellules d'un tableau FXML pour afficher un liens vers uen page web externe.
 * 
 * @author ETP8137 - Grégpoire Mathon
 * @since 2.0
 *
 */
public class TableCellFXMLFactory<T extends AbstractFXMLModele<I>, I> implements Callback<TableColumn<T, Object>, TableCell<T, Object>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableCell<T, Object> call(TableColumn<T, Object> param)
    {
        return new TableCellFXML<>();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSES PRIVEES ----------*/
}

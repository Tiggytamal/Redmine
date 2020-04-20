package fxml.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.fxml.ListeGetters;

public class ComboBoxEnumCellFactory implements Callback<ListView<ListeGetters>, ListCell<ListeGetters>>
{
    @Override
    public ListCell<ListeGetters> call(ListView<ListeGetters> param)
    {
        return new ListCell<ListeGetters>() 
        {
            @Override
            protected void updateItem(ListeGetters item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item == null || empty)
                    setText(null);
                else
                    setText(item.getAffichage());
            }
        };
    }
}

package control.view;

import java.io.IOException;

import control.CreerVuePatrimoineTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import view.ProgressDialog;

public class PatrimoineViewControl
{
    @FXML
    private GridPane backgroundPane;
    @FXML
    private Button creer;

    @FXML
    public void creerVue() throws IOException
    {
        CreerVuePatrimoineTask task = new CreerVuePatrimoineTask();
        ProgressDialog dialog = new ProgressDialog(task, "Vue patrimoine");
        dialog.show();
        dialog.startTask();
    }
}
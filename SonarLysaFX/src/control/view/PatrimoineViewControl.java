package control.view;

import control.ControlSonar;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import utilities.Statics;
import view.ProgressDialog;

public class PatrimoineViewControl
{
    @FXML
    private GridPane backgroundPane;
    @FXML
    private Button creer;

    private ControlSonar handler;

    @FXML
    public void initialize()
    {
        handler = new ControlSonar();
    }

    @FXML
    public void creerVue()
    {
        Task<Object> task = handler.creerVuePatrimoine();
        ProgressDialog dialog = new ProgressDialog(task, "Vue patrimoine");
        dialog.show();
        dialog.start();

    }
}

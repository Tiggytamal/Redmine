package junit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TesteurApp extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Scene scene = new Scene(new HBox(), 100, 100);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}

package fxml.factory;

import java.time.LocalTime;

import fxml.node.TimeSpinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Factory pour les valeurs du spinner pour augmenter et diminuer les heures et minutes
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class LocalTimeSpinnerValueFactory extends SpinnerValueFactory<LocalTime>
{
    /*---------- ATTRIBUTS ----------*/

    private TimeSpinner ts;

    /*---------- CONSTRUCTEURS ----------*/

    public LocalTimeSpinnerValueFactory(TimeSpinner ts)
    {
        this.ts = ts;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void decrement(int steps)
    {
        setValue(ts.getMode().decrement(getValue(), steps));
        ts.getMode().select(ts.getEditor());
    }

    @Override
    public void increment(int steps)
    {
        setValue(ts.getMode().increment(getValue(), steps));
        ts.getMode().select(ts.getEditor());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

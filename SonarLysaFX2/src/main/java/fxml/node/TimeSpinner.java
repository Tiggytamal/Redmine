package fxml.node;

import java.time.LocalTime;

import fxml.factory.LocalTimeSpinnerValueFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import model.enums.Mode;
import utilities.adapter.LocalTimeSpinnerConverter;

/**
 * Spinner limite à l'affichage et l'enregistrement d'une heure HH:mm
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TimeSpinner extends Spinner<LocalTime>
{

    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short WIDTH = 80;

    /** Propriété de l'objet contenant le mode d'edition */
    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS);

    /*---------- CONSTRUCTEURS ----------*/

    public TimeSpinner()
    {
        this(LocalTime.now());
    }
    
    public TimeSpinner(LocalTime time)
    {
        setEditable(true);
        setPrefWidth(WIDTH);
        TextField editeur = getEditor();

        // Convertisseur entre les String et LocalTime
        LocalTimeSpinnerConverter localTimeConverter = new LocalTimeSpinnerConverter();

        // Contrôle le texte edité pour verifer le format 00:00
        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, time, c -> c.getControlNewText().matches("[0-9]{0,2}:[0-9]{0,2}") ? c : null);

        LocalTimeSpinnerValueFactory valueFactory = new LocalTimeSpinnerValueFactory(this);
        valueFactory.setConverter(localTimeConverter);
        valueFactory.setValue(time);

        setValueFactory(valueFactory);
        editeur.setTextFormatter(textFormatter);

        // EventHandler sur la position du curseur pour choisir le mode d'incrementation
        editeur.addEventHandler(InputEvent.ANY, e -> {

            if (editeur.getCaretPosition() <= editeur.getText().indexOf(':'))
                mode.set(Mode.HOURS);
            else
                mode.set(Mode.MINUTES);
        });

        // Selection du nouveau mode
        mode.addListener((obs, oldMode, newMode) -> newMode.select(editeur));
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public ObjectProperty<Mode> modeProperty()
    {
        return mode;
    }

    public final Mode getMode()
    {
        return modeProperty().get();
    }

    public final void setMode(Mode mode)
    {
        modeProperty().set(mode);
    }
}

package view;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

public class TimeSpinner extends Spinner<LocalTime>
{

    /*---------- ATTRIBUTS ----------*/

    // Propriété de l'objet contenant le mode d'edition
    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS);

    /*---------- CONSTRUCTEURS ----------*/

    public TimeSpinner(LocalTime time)
    {
        setEditable(true);
        // Convertisseur entre les String et LocalTime
        LocalTimeConverter localTimeConverter = new LocalTimeConverter();

        // Contrôle le texte édité pour vérifer le format 00:00
        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, time, c -> c.getControlNewText().matches("[0-9]{0,2}:[0-9]{0,2}") ? c : null );
        
        LocalTimeSpinnerValueFactory valueFactory = new LocalTimeSpinnerValueFactory();
        valueFactory.setConverter(localTimeConverter);
        valueFactory.setValue(time);

        setValueFactory(valueFactory);
        getEditor().setTextFormatter(textFormatter);

        // EventHandler dur la position du curseur pour choisir le mode d'incrémentation
        getEditor().addEventHandler(InputEvent.ANY, e -> {

            if (getEditor().getCaretPosition() <= getEditor().getText().indexOf(':'))
                mode.set(Mode.HOURS);
            else
                mode.set(Mode.MINUTES);
        });
        
        // Selection du nouveau mode
        mode.addListener((obs, oldMode, newMode) -> newMode.select(this));
    }

    public TimeSpinner()
    {
        this(LocalTime.now());
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

    // Mode represents the unit that is currently being edited.
    // For convenience expose methods for incrementing and decrementing that unit, and for selecting the appropriate portion in a spinner's editor
    private enum Mode {
        HOURS {
            @Override
            LocalTime increment(LocalTime time, int steps)
            {
                return time.plusHours(steps);
            }

            @Override
            void select(TimeSpinner spinner)
            {
                int index = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, index);
            }
        },
        MINUTES {
            @Override
            LocalTime increment(LocalTime time, int steps)
            {
                return time.plusMinutes(steps);
            }

            @Override
            void select(TimeSpinner spinner)
            {
                int hrIndex = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(hrIndex + 1, hrIndex + 3);
            }
        };
        abstract LocalTime increment(LocalTime time, int steps);

        abstract void select(TimeSpinner spinner);

        LocalTime decrement(LocalTime time, int steps)
        {
            return increment(time, -steps);
        }
    }

    /**
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class LocalTimeConverter extends StringConverter<LocalTime>
    {
        @Override
        public String toString(LocalTime time)
        {
            return DateTimeFormatter.ofPattern("HH:mm").format(time);
        }

        @Override
        public LocalTime fromString(String string)
        {
            String[] tokens = string.split(":");
            return LocalTime.of(getIntField(tokens, 0), getIntField(tokens, 1));
        }

        private int getIntField(String[] tokens, int index)
        {
            if (tokens.length <= index || tokens[index].isEmpty())
                return 0;
            return Integer.parseInt(tokens[index]);
        }
    }

    /**
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class LocalTimeSpinnerValueFactory extends SpinnerValueFactory<LocalTime>
    {
        @Override
        public void decrement(int steps)
        {
            setValue(mode.get().decrement(getValue(), steps));
            mode.get().select(TimeSpinner.this);
        }

        @Override
        public void increment(int steps)
        {
            setValue(mode.get().increment(getValue(), steps));
            mode.get().select(TimeSpinner.this);
        }
    }
}
package model.enums;

import java.time.LocalTime;

import javafx.scene.control.TextField;

/**
 * Enumération représentant l'unité utilisée. Heures ou Minutes
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public enum Mode
{
    /*---------- ATTRIBUTS ----------*/

    HOURS {
        @Override
        public LocalTime increment(LocalTime time, int steps)
        {
            return time.plusHours(steps);
        }

        @Override
        public void select(TextField field)
        {
            int index = field.getText().indexOf(':');
            field.selectRange(0, index);
        }
    },
    MINUTES {
        @Override
        public LocalTime increment(LocalTime time, int steps)
        {
            return time.plusMinutes(steps);
        }

        @Override
        public void select(TextField field)
        {
            int hrIndex = field.getText().indexOf(':');
            field.selectRange(hrIndex + 1, hrIndex + 3);
        }
    };

    /*---------- CONSTRUCTEURS ----------*/

    public LocalTime decrement(LocalTime time, int steps)
    {
        return increment(time, -steps);
    }

    /*---------- METHODES ABSTRAITES ----------*/

    public abstract LocalTime increment(LocalTime time, int steps);

    public abstract void select(TextField spinner);

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

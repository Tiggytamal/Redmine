package utilities.adapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

/**
 * Convertisseur pour enregistrer les heures sous forme de String
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class LocalTimeSpinnerConverter extends StringConverter<LocalTime>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String toString(LocalTime time)
    {
        return DateTimeFormatter.ofPattern("HH:mm").format(time);
    }

    @Override
    public LocalTime fromString(String string)
    {
        String[] split = string.split(":");
        return LocalTime.of(getIntField(split, 0), getIntField(split, 1));
    }

    /*---------- METHODES PRIVEES ----------*/

    private int getIntField(String[] split, int index)
    {
        if (split.length <= index || split[index].isEmpty())
            return 0;
        return Integer.parseInt(split[index]);
    }
    
    /*---------- ACCESSEURS ----------*/
}

package model;

/**
 * Repr�sente la classe m�re des classes de mod�le
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public interface Modele
{

    public default String getString(String val)
    {
        return val == null ? "" : val;
    }

    public default boolean compare(Object objet, Object autre)
    {
        if (objet == null)
        {
            if (autre != null)
                return false;
        }
        else if (!objet.equals(autre))
            return false;
        return true;
    }
}

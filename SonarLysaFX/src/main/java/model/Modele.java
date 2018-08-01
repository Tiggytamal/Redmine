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
}

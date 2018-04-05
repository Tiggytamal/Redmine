package model;

/**
 * Représente la classe mère des classes de modèle
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class Modele
{
    
    protected String getString(String val)
    {
        return val == null ? "" : val;
    }
}

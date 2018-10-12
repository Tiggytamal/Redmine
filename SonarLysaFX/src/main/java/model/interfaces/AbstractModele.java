package model.interfaces;

import utilities.AbstractToStringImpl;
import utilities.Statics;

/**
 * Représente la classe mère des classes de modèle
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractModele extends AbstractToStringImpl
{
    
    protected AbstractModele() { }
    
    /**
     * Protège les {@code String} pour renvoyer une chaîne de caratères vide en cas d'objet null.
     * @param val
     * @return
     */
    protected String getString(String val)
    {
        return val == null ? Statics.EMPTY : val;
    }
}

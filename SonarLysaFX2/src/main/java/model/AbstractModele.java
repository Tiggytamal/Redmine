package model;

import utilities.AbstractToStringImpl;
import utilities.Statics;

/**
 * Représente la classe mère de tous les modèles.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractModele extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractModele()
    {}

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Protège les {@code String} pour renvoyer une chaîne de caratères vide en cas d'objet null.
     * 
     * @param val
     *            Valeur à tester.
     * @return
     *         la valeur, ou "" si elle est nulle.
     */
    protected final String getString(String val)
    {
        return val == null ? Statics.EMPTY : val;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return getClass() == obj.getClass();
        //@formatter:on
    }

    @Override
    public int hashCode()
    {
        return 31;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

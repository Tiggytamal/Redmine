package model.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import utilities.Statics;

/**
 * Représente la classe mère des classes de modèle
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractModele
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
    
    /**
     * Protège les listes pour renvoyer une {@code ArrayList} en cas d'objet null.
     * @param liste
     * @return
     */
    protected <T> List<T> getList(List<T> liste)
    {
        return liste == null ? new ArrayList<>() : liste;
    }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

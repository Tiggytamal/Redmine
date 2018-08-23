package model.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import utilities.Statics;

/**
 * Repr�sente la classe m�re des classes de mod�le
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractModele
{
    
    protected AbstractModele() { }
    
    /**
     * Prot�ge les {@code String} pour renvoyer une cha�ne de carat�res vide en cas d'objet null.
     * @param val
     * @return
     */
    protected String getString(String val)
    {
        return val == null ? Statics.EMPTY : val;
    }
    
    /**
     * Prot�ge les listes pour renvoyer une {@code ArrayList} en cas d'objet null.
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

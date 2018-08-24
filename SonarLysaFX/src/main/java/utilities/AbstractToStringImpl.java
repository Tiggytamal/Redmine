package utilities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Classe générique primaire d'implantation de la méthode toString en réflexion.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractToStringImpl
{
    protected AbstractToStringImpl() { }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

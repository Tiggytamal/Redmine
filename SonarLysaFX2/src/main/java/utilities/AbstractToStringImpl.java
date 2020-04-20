package utilities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Classe générique primaire d'implantation de la methode toString en reflexion.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractToStringImpl
{
    protected AbstractToStringImpl() 
    {}
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

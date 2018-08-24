package utilities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Classe g�n�rique primaire d'implantation de la m�thode toString en r�flexion.
 * 
 * @author ETP8137 - Gr�goire Mathon
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

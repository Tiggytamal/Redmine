package utilities.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import model.enums.TypeMetrique;

/**
 * Classe de gestion des {@link model.enums.TypeMetrique} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TypeMetriqueAdapter extends XmlAdapter<String, TypeMetrique>
{

    public TypeMetrique unmarshal(String v) throws Exception
    {
        if (v != null)
            return TypeMetrique.from(v);
        return null;
    }

    public String marshal(TypeMetrique v) throws Exception
    {
        if (v != null)
            return v.getValeur();
        return null;
    }
}

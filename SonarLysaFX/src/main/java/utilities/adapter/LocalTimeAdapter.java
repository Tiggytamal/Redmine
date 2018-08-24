package utilities.adapter;

import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Classe de gestion des {@link java.time.LocalTime} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime>
{
    public LocalTime unmarshal(String v) throws Exception
    {
        if (v != null)
            return LocalTime.parse(v);
        return null;
    }

    public String marshal(LocalTime v) throws Exception
    {
        if (v != null)
            return v.toString();
        return null;
    }
}

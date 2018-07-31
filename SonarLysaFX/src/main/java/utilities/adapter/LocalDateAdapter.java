package utilities.adapter;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Classe de gestion des {@code LocalDate} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate>
{
    public LocalDate unmarshal(String v) throws Exception
    {
        if (v != null)
            return LocalDate.parse(v);
        return null;
    }

    public String marshal(LocalDate v) throws Exception
    {
        if (v != null)
            return v.toString();
        return null;
    }
}

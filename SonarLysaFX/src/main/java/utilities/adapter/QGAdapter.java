package utilities.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import model.enums.QG;

/**
 * Classe de gestion des {@link model.enums.QG} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class QGAdapter extends XmlAdapter<String, QG>
{

    public QG unmarshal(String v) throws Exception
    {
        if (v != null)
            return QG.from(v);
        return null;
    }

    public String marshal(QG v) throws Exception
    {
        if (v != null)
            return v.getValeur();
        return null;
    }
}

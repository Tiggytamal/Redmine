package utilities.adapter;

import java.util.function.Function;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Niveau d'abstraction de l'adaptateur XML pour éviter la duplication du code dans chaque classe.
 * 
 * @author ETP8137 - Gregoire Mathon
 * @since 2.0
 * @param <V> TypeValue
 * @param <B> BoundType
 */
public abstract class AbstractXmlAdapter<V, B> extends XmlAdapter<V, B>
{
    /*---------- ATTRIBUTS ----------*/

    private Function<V, B> unmarshaller;
    private Function<B, V> marshaller;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractXmlAdapter(Function<V, B> unmarshaller, Function<B, V> marshaller)
    {
        this.unmarshaller = unmarshaller;
        this.marshaller = marshaller;
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public B unmarshal(V v) throws Exception
    {
        return v == null ? null : unmarshaller.apply(v);
    }

    @Override
    public V marshal(B b) throws Exception
    {
        return b == null ? null : marshaller.apply(b);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package utilities.adapter;

import java.util.function.Function;

import javax.persistence.AttributeConverter;

public abstract class AbstractAttributeConverter<V, B> implements AttributeConverter<V, B>
{
    /*---------- ATTRIBUTS ----------*/
    
    private Function<B, V> toEntity;
    private Function<V, B> toDataBase;
    
    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractAttributeConverter(Function<B, V> toEntity, Function<V, B> toDataBase)
    {
        this.toEntity = toEntity;
        this.toDataBase = toDataBase;
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public B convertToDatabaseColumn (V v)
    {
        return v == null ? null : toDataBase.apply(v);
    }
    
    @Override
    public V convertToEntityAttribute (B b)
    {
        return b == null ? null : toEntity.apply(b);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

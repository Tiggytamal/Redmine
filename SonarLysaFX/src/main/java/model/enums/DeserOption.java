package model.enums;

/**
 * Enum�ration pour g�rer la d�srialisation ou non des objets pour les tests. <br/>
 * Pour la production, il faut utiliser le mode AUCUNE.
 * 
 * @author ETP8137
 * @since 1.0
 */
public enum DeserOption 
{   
    AUCUNE,
    DESERIALISATION,
    SERIALISATION;
}

package model.enums;

/**
 * Représente les type de colonnes possibles
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public interface TypeCol extends TypeKey
{
 public abstract String getValeur();
 
 public abstract String getNomCol();
}

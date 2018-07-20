package model.enums;

/**
 * Représente les type de colonnes possibles des fichiers en lecture
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public interface TypeColR extends TypeKey
{
 public abstract String getValeur();
 
 public abstract String getNomCol();
}

package model.enums;

/**
 * Enum�ration repr�sentant tous les groupes de composants. ex : NPC
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum GroupeProjet 
{
    NPC("NPC"), 
    VIDE("");

    private String valeur;

    private GroupeProjet(String valeur)
    {
        this.valeur = valeur;
    }

    public String getValeur()
    {
        return valeur;
    }
}

package model.enums;

/**
 * Enumération représentant tous les groupes de composants. ex : NPC
 * 
 * @author ETP8137 - Grégoire Mathon
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

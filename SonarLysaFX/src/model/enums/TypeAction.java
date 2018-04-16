package model.enums;

/**
 * Réprésente les valeurs possibles de la colonne actions
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */ 
public enum TypeAction
{
    CREER(Valeur.CREER), 
    VERIFIER(Valeur.VERIFIER), 
    INCONNUE(Valeur.INCONNUE),
    ASSEMBLER(Valeur.ASSEMBLER);

    private final String valeur;

    private TypeAction(String valeur)
    {
        this.valeur = valeur;
    }

    @Override
    public String toString()
    {
        return valeur;
    }

    public static TypeAction from(String typeActionString)
    {
        switch (typeActionString)
        {
            case Valeur.CREER :
                return CREER;

            case Valeur.VERIFIER :
                return VERIFIER;
                
            case Valeur.ASSEMBLER :
                return ASSEMBLER;

            default :
                return INCONNUE;
        }
    }

    private static class Valeur
    {
        private Valeur() {}

        private static final String CREER = "A créer";
        private static final String VERIFIER = "A vérifier";
        private static final String ASSEMBLER = "A assembler";
        private static final String INCONNUE = "Inconnue";
    }
}
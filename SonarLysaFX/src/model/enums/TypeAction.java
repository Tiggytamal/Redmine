package model.enums;

/**
 * R�pr�sente les valeurs possibles de la colonne actions
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */ 
public enum TypeAction
{
    CREER(Valeur.CREER), 
    VERIFIER(Valeur.VERIFIER), 
    VIDE(Valeur.VIDE),
    ASSEMBLER(Valeur.ASSEMBLER),
    CLOTURER(Valeur.CLOTURER);

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
                
            case Valeur.CLOTURER :
                return CLOTURER;

            default :
                return VIDE;
        }
    }

    private static class Valeur
    {
        private Valeur() {}

        private static final String CREER = "A cr�er";
        private static final String VERIFIER = "A v�rifier";
        private static final String ASSEMBLER = "A assembler";
        private static final String VIDE = "";
        private static final String CLOTURER = "A cl�turer";
    }
}
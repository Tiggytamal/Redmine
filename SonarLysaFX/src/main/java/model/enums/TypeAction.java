package model.enums;

import utilities.Statics;

/**
 * Réprésente les valeurs possibles de la colonne actions
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */ 
public enum TypeAction
{
    /*---------- ATTRIBUTS ----------*/

    CREER(Valeur.CREER), 
    VERIFIER(Valeur.VERIFIER), 
    VIDE(Valeur.VIDE),
    ASSEMBLER(Valeur.ASSEMBLER),
    CLOTURER(Valeur.CLOTURER),
    ABANDONNER(Valeur.ABANDONNER),
    RELANCER(Valeur.RELANCER),
    REOUV(Valeur.REOUV);

    private final String valeur;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeAction(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public String getValeur()
    {
        return valeur;
    }

    public static TypeAction from(String typeAction)
    {
        switch (typeAction)
        {
            case Valeur.CREER :
                return CREER;

            case Valeur.VERIFIER :
                return VERIFIER;
                
            case Valeur.ASSEMBLER :
                return ASSEMBLER;
                
            case Valeur.CLOTURER :
                return CLOTURER;
                
            case Valeur.ABANDONNER :
                return ABANDONNER;
                
            case Valeur.RELANCER :
                return RELANCER;  
                
            case Valeur.REOUV :
                return REOUV;

            default :
                return VIDE;
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    private static final class Valeur
    {
        private static final String CREER = "A créer";
        private static final String VERIFIER = "A vérifier";
        private static final String ASSEMBLER = "A assembler";
        private static final String CLOTURER = "A clôturer";
        private static final String ABANDONNER = "A abandonner";
        private static final String RELANCER = "A relancer";
        private static final String REOUV = "A réouvrir";
        private static final String VIDE = Statics.EMPTY;
        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeAction$Valeur.");
        }
    }
}

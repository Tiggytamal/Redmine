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
    REOUV(Valeur.REOUV),
    AJOUTCOMM(Valeur.AJOUTCOMM);

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
                
            case Valeur.AJOUTCOMM :
                return AJOUTCOMM;

            default :
                return VIDE;
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    private static final class Valeur
    {
        private static final String CREER = "Créer";
        private static final String VERIFIER = "Vérifier";
        private static final String ASSEMBLER = "Assembler";
        private static final String CLOTURER = "Clôturer";
        private static final String ABANDONNER = "Abandonner";
        private static final String RELANCER = "Relancer";
        private static final String REOUV = "Réouvrir";
        private static final String AJOUTCOMM = "Ajouter com.";
        private static final String VIDE = Statics.EMPTY;
        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeAction$Valeur.");
        }
    }
}

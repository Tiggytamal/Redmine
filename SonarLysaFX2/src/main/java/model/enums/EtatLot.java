package model.enums;

/**
 * Enumeration permettant de classer l'etat d'un lot RTC et dans quel environnement il a été pousse.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum EtatLot
{
    /*---------- ATTRIBUTS ----------*/

    NOUVEAU(Valeur.NOUVEAU),
    DEVTU(Valeur.DEVTU),
    TFON(Valeur.TFON),
    VMOE(Valeur.VMOE),
    MOA(Valeur.MOA),
    VMOA(Valeur.VMOA),
    INSTALLE(Valeur.INSTALLE),
    EDITION(Valeur.EDITION),
    TERMINE(Valeur.TERMINE),
    ABANDONNE(Valeur.ABANDONNE),
    INCONNU(Valeur.INCONNU);
    
    private final String valeur;
    
    /*---------- CONSTRUCTEURS ----------*/

    EtatLot(String valeur)
    {
        this.valeur = valeur;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public static EtatLot from(String envString)
    {
        if (envString == null || envString.isEmpty())
            return EtatLot.INCONNU;
        
        switch(envString)
        {
            case Valeur.NOUVEAU :
                return NOUVEAU;
                
            case Valeur.DEVTU :
                return DEVTU;
                
            case Valeur.TFON :
                return TFON;
                
            case Valeur.VMOE :
                return VMOE;
                
            case Valeur.VMOA :
                return VMOA;
                
            case Valeur.INSTALLE:
                return INSTALLE;
                
            case Valeur.EDITION :
                return EDITION;
                
            case Valeur.ABANDONNE :
                return ABANDONNE;
                
            case Valeur.TERMINE :
                return TERMINE;
                
            case Valeur.MOA :
                return MOA;
                
            default :
                return INCONNU;
        }      
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
    
    /*---------- CLASSES PRIVEES ----------*/
    
    private static final class Valeur
    {
        private static final String NOUVEAU = "Nouveau";
        private static final String DEVTU = "En DEV-TU";
        private static final String TFON = "TFON";
        private static final String VMOE = "En Vérification MOE";
        private static final String VMOA = "En Validation MOA";
        private static final String INSTALLE = "Installé";
        private static final String MOA = "Candidat pour la Validation MOA";
        private static final String EDITION = "Livré à l'Edition";
        private static final String ABANDONNE = "Abandonné";
        private static final String TERMINE = "Terminé";
        private static final String INCONNU = "INCONNU"; 
        
        // Contructeur prive empechant l'instanciation
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Environnement$Valeur");
        }
    }
}

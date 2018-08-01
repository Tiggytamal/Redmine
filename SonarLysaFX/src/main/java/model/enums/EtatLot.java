package model.enums;

/**
 * Enumeration permettant de classer l'�tat d'un lot RTC et dans quel environnement il a �t� pouss�.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public enum EtatLot
{
    NOUVEAU(Valeur.NOUVEAU),
    DEVTU(Valeur.DEVTU),
    TFON(Valeur.TFON),
    VMOE(Valeur.VMOE),
    MOA(Valeur.MOA),
    VMOA(Valeur.VMOA),
    EDITION(Valeur.EDITION),
    TERMINE(Valeur.TERMINE),
    ABANDONNE(Valeur.ABANDONNE),
    INCONNU(Valeur.INCONNU);
    
    private final String valeur;
    
    private EtatLot(String valeur)
    {
        this.valeur = valeur;
    }
    
    public String getValeur()
    {
        return valeur;
    }
    
    public static EtatLot from(String envString)
    {
        if (envString == null)
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
    
    private static final class Valeur
    {
        private static final String NOUVEAU = "Nouveau";
        private static final String DEVTU = "En DEV-TU";
        private static final String TFON = "TFON";
        private static final String VMOE = "En V�rification MOE";
        private static final String VMOA = "En Validation MOA";
        private static final String MOA = "Candidat pour la Validation MOA";
        private static final String EDITION = "Livr� � l'Edition";
        private static final String ABANDONNE = "Abandonn�";
        private static final String TERMINE = "Termin�";
        private static final String INCONNU = "INCONNU"; 
        
        // Contructeur priv� emp�chant l'instanciation
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Environnement$Valeur");
        }
    }
}

package model.enums;

/**
 * Enumeration permettant de classer l'état d'un lot RTC et dans quel environnement il a été poussé.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum Environnement
{
    NOUVEAU(Valeur.NOUVEAU),
    DEVTU(Valeur.DEVTU),
    TFON(Valeur.TFON),
    VMOE(Valeur.VMOE),
    VMOA(Valeur.VMOA),
    EDITION(Valeur.EDITION),
    ABANDONNE(Valeur.ABANDONNE),
    TERMINE(Valeur.TERMINE),
    INCONNU(Valeur.INCONNU);
    
    private final String string;
    
    private Environnement(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public static Environnement getEnvironnement(String envString)
    {
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
                
            default :
                if (envString.contains(Valeur.EDITION))
                    return EDITION;
                return INCONNU;
        }
        
    }
    
    private static class Valeur
    {
        private Valeur() {}
        
        private static final String NOUVEAU = "NOUVEAU";
        private static final String DEVTU = "DEVTU";
        private static final String TFON = "TFON";
        private static final String VMOE = "VMOE";
        private static final String VMOA = "VMOA";
        private static final String EDITION = "EDITION";
        private static final String ABANDONNE = "ABANDONNE";
        private static final String TERMINE = "TERMINE";
        private static final String INCONNU = "INCONNU";
    }
}
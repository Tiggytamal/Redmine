package model.enums;

/**
 * Enumération de tous les types de matères
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum Matiere 
{
    JAVA(Valeur.JAVA),
    DATASTAGE(Valeur.DATASTAGE),
    JAVASCRIPT(Valeur.JAVASCRIPT),
    PHP(Valeur.PHP);
    
    private final String string;
    
    private Matiere(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public static Matiere getMatiere(String matiere)
    {
        switch(matiere)
        {
            case Valeur.JAVA :
                return JAVA;
            case Valeur.DATASTAGE :
                return DATASTAGE;
            case Valeur.JAVASCRIPT :
                return JAVASCRIPT;
            case Valeur.PHP :
                return PHP;
            default :
                throw new IllegalArgumentException("Matière inconnue");
        }        
    }
    
    private static class Valeur
    {
        private Valeur() {}
        
        private static final String JAVA = "JAVA";
        private static final String DATASTAGE = "DATASTAGE";
        private static final String JAVASCRIPT = "JAVASCRIPT";
        private static final String PHP = "PHP";
    }
}

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
    
    public static Matiere from(String matiere)
    {
        switch(matiere.trim())
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
                throw new IllegalArgumentException("Matière inconnue :" + matiere);
        }        
    }
    
    private static class Valeur
    {        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Matiere$Valeur");
        }
        
        public static final String JAVA = "JAVA";
        public static final String DATASTAGE = "DATASTAGE";
        public static final String JAVASCRIPT = "JAVASCRIPT";
        public static final String PHP = "PHP";
    }
}

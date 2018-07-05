package model.enums;

/**
 * Enumération de tous les types de matères
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum Matiere 
{
    /*---------- ATTRIBUTS ----------*/

    JAVA(Valeur.JAVA, TypeMail.SUIVIJAVA),
    DATASTAGE(Valeur.DATASTAGE, TypeMail.SUIVIDATASTAGE),
    COBOL(Valeur.COBOL, TypeMail.SUIVICOBOL);
    
    private final String string;
    private final TypeMail typeMail;
    
    /*---------- CONSTRUCTEURS ----------*/

    private Matiere(String string, TypeMail typeMail)
    {
        this.string = string;
        this.typeMail = typeMail;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public static Matiere from(String matiere)
    {
        switch(matiere.trim())
        {
            case Valeur.JAVA :
                return JAVA;
            case Valeur.DATASTAGE :
                return DATASTAGE;
            case Valeur.COBOL :
                return COBOL;
            default :
                throw new IllegalArgumentException("Matière inconnue :" + matiere);
        }        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public TypeMail getTypeMail()
    {
        return typeMail;
    }
    
    /*---------- CLASSES PRIVEES ----------*/
    
    private static class Valeur
    {        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Matiere$Valeur");
        }
        
        public static final String JAVA = "JAVA";
        public static final String DATASTAGE = "DATASTAGE";
        public static final String COBOL = "COBOL";
    }
}

package model.enums;

/**
 * Enum�ration de tous les types de mat�res
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public enum Matiere 
{
    /*---------- ATTRIBUTS ----------*/

    JAVA(Valeur.JAVA, TypeMail.SUIVIJAVA),
    DATASTAGE(Valeur.DATASTAGE, TypeMail.SUIVIDATASTAGE),
    COBOL(Valeur.COBOL, TypeMail.SUIVICOBOL);
    
    private final String valeur;
    private final TypeMail typeMail;
    
    /*---------- CONSTRUCTEURS ----------*/

    private Matiere(String valeur, TypeMail typeMail)
    {
        this.valeur = valeur;
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
                throw new IllegalArgumentException("Mati�re inconnue :" + matiere);
        }        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
    
    public TypeMail getTypeMail()
    {
        return typeMail;
    }
    
    /*---------- CLASSES PRIVEES ----------*/
    
    private static final class Valeur
    {        
        public static final String JAVA = "JAVA";
        public static final String DATASTAGE = "DATASTAGE";
        public static final String COBOL = "COBOL";
        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Matiere$Valeur");
        }
    }
}

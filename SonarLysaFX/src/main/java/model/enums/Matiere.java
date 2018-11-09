package model.enums;

/**
 * Enumération de tous les types de matères
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum Matiere
{
    /*---------- ATTRIBUTS ----------*/

    JAVA(Valeur.JAVA, TypeRapport.SUIVIJAVA),
    DATASTAGE(Valeur.DATASTAGE, TypeRapport.SUIVIDATASTAGE),
    COBOL(Valeur.COBOL, TypeRapport.SUIVICOBOL),
    ANDROID(Valeur.ANDROID, TypeRapport.ANDROID),
    IOS(Valeur.IOS, TypeRapport.IOS);
    
    private final String valeur;
    private final TypeRapport typeRapport;
    
    /*---------- CONSTRUCTEURS ----------*/

    private Matiere(String valeur, TypeRapport typeRapport)
    {
        this.valeur = valeur;
        this.typeRapport = typeRapport;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public static Matiere from(String matiere)
    {
        switch(matiere)
        {
            case Valeur.JAVA :
                return JAVA;
                
            case Valeur.DATASTAGE :
                return DATASTAGE;
                
            case Valeur.COBOL :
                return COBOL;
                
            case Valeur.ANDROID :
                return ANDROID;
                
            case Valeur.IOS :
                return IOS;
                
            default :
                throw new IllegalArgumentException("model.enums.Matiere.from - matière envoyée inconnue : " + matiere);
        }        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
    
    public TypeRapport getTypeRapport()
    {
        return typeRapport;
    }
    
    /*---------- CLASSES PRIVEES ----------*/
    
    private static final class Valeur
    {        
        public static final String JAVA = "JAVA";
        public static final String DATASTAGE = "DATASTAGE";
        public static final String COBOL = "COBOL";
        public static final String ANDROID = "ANDROID";
        public static final String IOS = "IOS";
        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Matiere$Valeur");
        }
    }
}

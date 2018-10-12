package model.enums;

/**
 * Enum�ration de tous les types de mat�res
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public enum Matiere 
{
    /*---------- ATTRIBUTS ----------*/

    JAVA(Valeur.JAVA, TypeRapport.SUIVIJAVA),
    DATASTAGE(Valeur.DATASTAGE, TypeRapport.SUIVIDATASTAGE),
    COBOL(Valeur.COBOL, TypeRapport.SUIVICOBOL);
    
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
        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Matiere$Valeur");
        }
    }
}

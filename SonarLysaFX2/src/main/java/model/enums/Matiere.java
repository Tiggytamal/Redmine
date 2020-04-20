package model.enums;

import static utilities.Statics.proprietesXML;

/**
 * Enumeration de tous les types de materes
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum Matiere
{
    /*---------- ATTRIBUTS ----------*/

    JAVA(Valeur.JAVA),
    DATASTAGE(Valeur.DATASTAGE),
    COBOL(Valeur.COBOL),
    ANDROID(Valeur.ANDROID),
    IOS(Valeur.IOS),
    ANGULAR(Valeur.ANGULAR),
    INCONNUE(Valeur.INCONNUE);
    
    private final String valeur;
    
    /*---------- CONSTRUCTEURS ----------*/

    Matiere(String valeur)
    {
        this.valeur = valeur;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public static Matiere from(String matiere)
    {
        if (matiere == null || matiere.isEmpty())
            throw new IllegalArgumentException("model.enums.Matiere.from - valeur envoyée nulle ou vide.");
        
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
                
            case Valeur.ANGULAR :
                return ANGULAR;
                
            default :
                throw new IllegalArgumentException("model.enums.Matiere.from - valeur non gérée : " + matiere);
        }        
    }
    
    /**
     * Teste un composant MobileCenter et retourne la matière de celui-ci.
     * 
     * @param nom
     *            Nom du composant à tester.
     * @return
     *         La matière du composant.
     */
    public static Matiere testMatiereCompoMC(String nomCompo)
    {
        String filtreAngular = proprietesXML.getMapParams().get(Param.FILTREANGULAR);
        if (nomCompo.contains(filtreAngular))
            return Matiere.ANGULAR;

        String filtreIos = proprietesXML.getMapParams().get(Param.FILTREIOS);
        if (nomCompo.contains(filtreIos))
            return Matiere.IOS;

        String filtreAndroid = proprietesXML.getMapParams().get(Param.FILTREANDROID);
        if (nomCompo.contains(filtreAndroid))
            return Matiere.ANDROID;

        return Matiere.JAVA;
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
        public static final String JAVA = "JAVA";
        public static final String DATASTAGE = "DATASTAGE";
        public static final String COBOL = "COBOL";
        public static final String ANDROID = "ANDROID";
        public static final String IOS = "IOS";
        public static final String ANGULAR = "ANGULAR";
        public static final String INCONNUE ="INCONNUE";
        
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Matiere$Valeur");
        }
    }
}

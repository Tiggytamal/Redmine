package model.enums;

public enum TypeMajSuivi 
{
    /*---------- ATTRIBUTS ----------*/

    JAVA("Maj Fichier de Suivi JAVA"), 
    DATASTAGE("Maj Fichier de Suivi DataStage"), 
    MULTI("Maj Fichiers de Suivi"), 
    COBOL("Maj Fichier de Suivi COBOL");

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeMajSuivi(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
}

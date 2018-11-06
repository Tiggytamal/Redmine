package model.enums;

/**
 * Option pour la mise à jour des fichiers de suivi.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypeMajSuivi 
{
    /*---------- ATTRIBUTS ----------*/

    JAVA("Maj Fichier de Suivi JAVA", 7), 
    DATASTAGE("Maj Fichier de Suivi DataStage", 6),
    MULTI("Maj Fichiers de Suivi", 12), 
    COBOL("Maj Fichier de Suivi COBOL", 6),
    ANDROID("Maj Fichier de Suivi ANDROID", 6),
    IOS("Maj Fichier de Suivi IOS", 6),
    NUIT("Maj Fichier de Suivi avec Sonar", 13);

    private String valeur;
    private int nbreEtapes;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeMajSuivi(String valeur, int nbreEtapes)
    {
        this.valeur = valeur;
        this.nbreEtapes = nbreEtapes;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
    
    public int getNbreEtapes()
    {
        return nbreEtapes;
    }
}

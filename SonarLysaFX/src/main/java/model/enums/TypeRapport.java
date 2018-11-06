package model.enums;

/**
 * Enum�ration r�pertoriant tous les types de rapport � cr�er
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public enum TypeRapport
{
    /*---------- ATTRIBUTS ----------*/

    SUIVIJAVA("R�sum� du traitement de suivi des anomalies JAVA", "rapport JAVA - "),
    SUIVIDATASTAGE("R�sum� du traitement de suivi des anomalies DATASTAGE", "rapport DATASTAGE - "),
    SUIVICOBOL("R�sum� du traitement de suivi des anomalies COBOL", "rapport COBOL - "),
    ANDROID("R�sum� du traitement de suivi des anomalies Andro�d", "rapport Andro�d - "),
    IOS("R�sum� du traitement de suivi des anomalies iOS", "rapport iOS - "),
    VUEAPPS("R�sum� du traitement de la cr�ation des vues par application", "rapport Vues Applications - "),
    PURGESONAR("Liste des composants purg�s", "rapport Purge Sonar - ");
    
    // D�but du rapport
    private String debut;
    
    // Nom du fichier
    private String nomFichier;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeRapport(String debut, String nomFichier)
    {
        this.debut = debut;
        this.nomFichier = nomFichier;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getDebut()
    {
        return debut;
    }
    
    public String getNomFichier()
    {
        return nomFichier;
    }
}

package model.enums;

/**
 * Enumération répertoriant tous les types de rapport à créer
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeRapport
{
    /*---------- ATTRIBUTS ----------*/

    SUIVIJAVA("Résumé du traitement de suivi des anomalies JAVA", "rapport JAVA - "),
    SUIVIDATASTAGE("Résumé du traitement de suivi des anomalies DATASTAGE", "rapport DATASTAGE - "),
    SUIVICOBOL("Résumé du traitement de suivi des anomalies COBOL", "rapport COBOL - "),
    VUEAPPS("Résumé du traitement de la création des vues par application", "rapport Vues Applications - "),
    PURGESONAR("Liste des composants purgés", "rapport Purge Sonar - ");
    
    // Début du rapport
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

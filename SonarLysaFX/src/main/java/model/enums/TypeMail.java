package model.enums;

/**
 * Enumération répertoriant tous les types de mail à envoyer.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum TypeMail
{
    /*---------- ATTRIBUTS ----------*/

    SUIVIJAVA("JAVA - Rapport MAJ fichier Qualimétrie du ", "Bonjour,\nVoici un résumé du traitement journalier du fichier de suivi des anomalies JAVA du "),
    SUIVIDATASTAGE("DATASTAGE - Rapport MAJ fichier Qualimétrie du ", "Bonjour,\nVoici un résumé du traitement journalier du fichier de suivi des anomalies DATASTAGE du "),
    SUIVICOBOL("COBOL - Rapport MAJ fichier Qualimétrie JAVA du ", "Bonjour,\nVoici un résumé du traitement journalier du fichier de suivi des anomalies COBOL du "),
    VUEAPPS("Rapport Création vues par Applications du ", "Bonjour,\nVoici un résumé du traitement de la création des vues par application du "),
    PURGESONAR("Rapport Purge composants SonarQube du ","Bonjour,\nVoici la liste des composants purgés du ");
    
    // Titre du mail
    private String titre;
    
    // Début du corps du mail
    private String debut;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    private TypeMail(String titre, String debut)
    {
        this.titre = titre;
        this.debut = debut;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getTitre()
    {
        return titre;
    }
    
    public String getDebut()
    {
        return debut;
    }
}

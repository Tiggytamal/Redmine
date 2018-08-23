package model.enums;

/**
 * Enum�ration r�pertoriant tous les types de mail � envoyer.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public enum TypeMail
{
    /*---------- ATTRIBUTS ----------*/

    SUIVIJAVA("JAVA - Rapport MAJ fichier Qualim�trie du ", "Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies JAVA du "),
    SUIVIDATASTAGE("DATASTAGE - Rapport MAJ fichier Qualim�trie du ", "Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies DATASTAGE du "),
    SUIVICOBOL("COBOL - Rapport MAJ fichier Qualim�trie JAVA du ", "Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies COBOL du "),
    VUEAPPS("Rapport Cr�ation vues par Applications du ", "Bonjour,\nVoici un r�sum� du traitement de la cr�ation des vues par application du "),
    PURGESONAR("Rapport Purge composants SonarQube du ","Bonjour,\nVoici la liste des composants purg�s du ");
    
    // Titre du mail
    private String titre;
    
    // D�but du corps du mail
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

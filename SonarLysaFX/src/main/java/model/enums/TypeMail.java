package model.enums;

/**
 * Enumération répertoriant tous les types de mail à envoyer.
 * @author ETP8137 - Grégoire Mathon
 *
 */
public enum TypeMail 
{
    /*---------- ATTRIBUTS ----------*/

    SUIVI("Rapport MAJ fichier Qualimétrie du ", "Bonjour,\nVoici un résumé du traitement journalier du fichier de suivi des anomalies du "),
    VUEAPPS("Rapport Création vues par Applications ", "Bonjour,\nVoici un résumé du traitement de la création des vues par application du ");
    
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
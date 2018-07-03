package model.enums;

/**
 * Enum�ration r�pertoriant tous les types de mail � envoyer.
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public enum TypeMail 
{
    /*---------- ATTRIBUTS ----------*/

    SUIVI("Rapport MAJ fichier Qualim�trie du ", "Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies du "),
    VUEAPPS("Rapport Cr�ation vues par Applications ", "Bonjour,\nVoici un r�sum� du traitement de la cr�ation des vues par application du ");
    
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
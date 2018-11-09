package model.enums;

import utilities.TechnicalException;

public enum EtatAnoRTC 
{
    /*---------- ATTRIBUTS ----------*/

    NOUVELLE(Valeur.NOUVELLE, "Accepter"),
    OUVERTE(Valeur.OUVERTE, "Commencer à travailler"),
    ENCOURS(Valeur.ENCOURS, "Terminer le travail"),
    DENOUEMENT(Valeur.DENOUEMENT, "Reprendre"),
    RESOLUE(Valeur.RESOLUE, "Clore"),
    VMOE(Valeur.VMOE, "Vérifier OK"),
    VERIFIEE(Valeur.VERIFIEE, "Clore"),
    VMOA(Valeur.VMOA, "Valider et clore"),
    REJETEE(Valeur.REJETEE, ""),
    EDITEUR(Valeur.EDITEUR, "Terminer le travail"),
    ATTEDITEUR(Valeur.ATTEDITEUR, "Tester correctif"),
    CLOSE(Valeur.CLOSE, ""),
    ABANDONNEE(Valeur.ABANDONNEE, ""),
    REOUVERTE(Valeur.REOUVERTE, "Commencer à travailler");
    
    // valeur dans RTC de l'état
    private String valeur;
    
    // non de l'action permettant de faire avancer le workflow
    private String action;
    
    /*---------- CONSTRUCTEURS ----------*/

    private EtatAnoRTC(String valeur, String action)
    {
        this.valeur = valeur;
        this.action = action;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public static EtatAnoRTC from(String valeur)
    {
        if (valeur == null || valeur.isEmpty())
            throw new TechnicalException("model.enums.EtatAnoRTC.from - etat envoyé nul ou vide.", null);
        
        switch (valeur)
        {
            case Valeur.NOUVELLE:
                return NOUVELLE;
                
            case Valeur.OUVERTE:
                return OUVERTE;
                
            case Valeur.ENCOURS:
                return ENCOURS;
                
            case Valeur.RESOLUE:
                return RESOLUE;
                
            case Valeur.VMOE:
                return VMOE;
                
            case Valeur.VERIFIEE:
                return VERIFIEE;
                
            case Valeur.VMOA:
                return VMOA;
                
            case Valeur.CLOSE:
                return CLOSE;
                
            case Valeur.ABANDONNEE:
                return ABANDONNEE;
                
            case Valeur.REOUVERTE:
                return REOUVERTE;
                
            case Valeur.DENOUEMENT:
                return DENOUEMENT;
                
            case Valeur.REJETEE:
                return REJETEE;
                
            case Valeur.EDITEUR:
                return EDITEUR;
                
            case Valeur.ATTEDITEUR:
                return ATTEDITEUR;

            default:
                throw new TechnicalException("model.enums.EtatAnoRTC.from - etat envoyé inconnu :" + valeur, null);
        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getValeur()
    {
        return valeur;
    }
    
    public String getAction()
    {
        return action;
    }
    
    /*---------- CLASSES PRIVEES ----------*/
    
    private static final class Valeur
    {
        private static final String NOUVELLE = "Nouvelle";
        private static final String OUVERTE = "Ouverte";
        private static final String ENCOURS = "En cours";
        private static final String RESOLUE = "Résolue";
        private static final String VMOE = "En attente vérification MOE";
        private static final String VERIFIEE = "Vérifiée";
        private static final String VMOA = "En attente validation/homologation MOA";
        private static final String CLOSE = "Close";
        private static final String REOUVERTE = "Réouverte";
        private static final String ABANDONNEE = "Abandonnée";
        private static final String DENOUEMENT = "En attente de dénouement";
        private static final String REJETEE = "Rejetée";
        private static final String EDITEUR = "Correctif éditeur testé";
        private static final String ATTEDITEUR = "En attente correctif éditeur";
        
        // Contructeur privé empéchant l'instanciation
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Environnement$Valeur");
        }
    }
}

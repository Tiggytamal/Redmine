package model.enums;

import utilities.TechnicalException;

public enum EtatAnoRTC 
{
    /*---------- ATTRIBUTS ----------*/

    NOUVELLE(Valeur.NOUVELLE, "Accepter"),
    OUVERTE(Valeur.OUVERTE, "Commencer � travailler"),
    ENCOURS(Valeur.ENCOURS, "Terminer le travail"),
    RESOLUE(Valeur.RESOLUE, "Clore"),
    VMOE(Valeur.VMOE, "V�rifier OK"),
    VERIFIEE(Valeur.VERIFIEE, "Clore"),
    VMOA(Valeur.VMOA, "Valider et clore"),
    CLOSE(Valeur.CLOSE, ""),
    REOUVERTE(Valeur.REOUVERTE, "Commencer � travailler");
    
    // valeur dans RTC de l'�tat
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
            throw new TechnicalException("model.enums.EtatAnoRTC.from - etat envoy� nul ou vide.", null);
        
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
                
            case Valeur.REOUVERTE:
                return REOUVERTE;

            default:
                throw new TechnicalException("model.enums.EtatAnoRTC.from - etat envoy� inconnu :" + valeur, null);
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
        private static final String RESOLUE = "R�solue";
        private static final String VMOE = "En attente v�rification MOE";
        private static final String VERIFIEE = "V�rifi�e";
        private static final String VMOA = "En attente validation/homologation MOA";
        private static final String CLOSE = "Close";
        private static final String REOUVERTE = "R�ouverte";
        
        // Contructeur priv� emp�chant l'instanciation
        private Valeur() 
        {
            throw new AssertionError("Classe non instanciable : model.enums.Environnement$Valeur");
        }
    }
}

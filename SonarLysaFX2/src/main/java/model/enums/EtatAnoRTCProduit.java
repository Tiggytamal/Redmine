package model.enums;

import static utilities.Statics.EMPTY;

/**
 * Enumération regroupant tous les états possibles d'une anomalie d'un espace RTC Produit.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public enum EtatAnoRTCProduit
{
    /*---------- ATTRIBUTS ----------*/

    NOUVELLE(Valeur.NOUVELLE, "Affecter"),
    AFFECTEE(Valeur.AFFECTEE, "Analyser"),
    ANALYSE(Valeur.ANALYSE, "Commencer la correction"),
    CORRECTION(Valeur.CORRECTION, "Correction terminée"),
    CORRIGEE(Valeur.CORRIGEE, "Livrer MOE"),
    REJETE(Valeur.REJETE, EMPTY),
    VMOE(Valeur.VMOE, "Valider MOE"),
    VERIFIEE(Valeur.VERIFIEE, "Livrer à la MOA"),
    VMOA(Valeur.VMOA, "Valider OK"),
    CLOSE(Valeur.CLOSE, EMPTY),
    ENATTENTE(Valeur.ENATTENTE, "Affecter"),
    ABANDONNEE(Valeur.ABANDONNEE, EMPTY);

    // valeur dans RTC de l'etat
    private String valeur;

    // non de l'action permettant de faire avancer le workflow
    private String action;

    /*---------- CONSTRUCTEURS ----------*/

    EtatAnoRTCProduit(String valeur, String action)
    {
        this.valeur = valeur;
        this.action = action;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static EtatAnoRTCProduit from(String valeur)
    {
        if (valeur == null || valeur.isEmpty())
            throw new IllegalArgumentException("model.enums.EtatAnoRTCProduit.from - état envoyé inconnu : " + valeur, null);

        switch (valeur)
        {
            case Valeur.NOUVELLE:
                return NOUVELLE;

            case Valeur.AFFECTEE:
                return AFFECTEE;

            case Valeur.ANALYSE:
                return ANALYSE;

            case Valeur.CORRECTION:
                return CORRECTION;

            case Valeur.CORRIGEE:
                return CORRIGEE;

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

            case Valeur.ENATTENTE:
                return ENATTENTE;
                
            case Valeur.REJETE:
                return REJETE;

            default:
                throw new IllegalArgumentException("model.enums.EtatAnoRTCProduit.from - état envoyé inconnu : " + valeur, null);
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
        private static final String NOUVELLE = "1.Nouvelle";
        private static final String AFFECTEE = "2.Affectée";
        private static final String ANALYSE = "3.Analyse";
        private static final String CORRECTION = "4.En cours de correction";
        private static final String CORRIGEE = "5.Corrigée MOE";
        private static final String VMOE = "6. Livrée MOE";
        private static final String VERIFIEE = "7. Validée MOE";
        private static final String VMOA = "8.Livrée MOA";
        private static final String CLOSE = "9.Validée";
        private static final String ABANDONNEE = "Abandonnée";
        private static final String ENATTENTE = "En attente";
        private static final String REJETE = "Rejeté";

        // Contructeur prive empechant l'instanciation
        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.EtatAnoRTCProduit$Valeur");
        }
    }
}

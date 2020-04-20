package model.enums;

import model.bdd.ComposantBase;

/**
 * Enumération des différents états d'un code application
 * 
 * @author ETP8137 - Grégoire
 * @since 2.0
 */
public enum EtatCodeAppli
{
    /*---------- ATTRIBUTS ----------*/

    OK("OK"),
    ERREUR("ERREUR"),
    NA("N/A");

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    EtatCodeAppli(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String toString()
    {
        return getValeur();
    }

    /**
     * Calcul d'un code application depuis les informations d'un composant
     * 
     * @param compoBase
     *                  Composant à traiter
     * @return
     *         L'état du code appli (OK, ERREUR, NA)
     */
    public static EtatCodeAppli calculCodeAppli(ComposantBase compoBase)
    {
        if (!compoBase.isControleAppli())
            return EtatCodeAppli.NA;

        if (compoBase.getAppli() != null && compoBase.getAppli().isReferentiel())
            return EtatCodeAppli.OK;

        return EtatCodeAppli.ERREUR;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getValeur()
    {
        return valeur;
    }
}

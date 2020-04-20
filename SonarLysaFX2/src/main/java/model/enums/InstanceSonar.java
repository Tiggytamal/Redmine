package model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration des types de serveurs SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum InstanceSonar
{
    /*---------- ATTRIBUTS ----------*/

    LEGACY(Valeur.LEGACY),
    MOBILECENTER(Valeur.MOBILECENTER);

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    InstanceSonar(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static InstanceSonar from(String valeur)
    {
        if (valeur == null || valeur.isEmpty())
            throw new IllegalArgumentException("model.enums.InstanceSonar.from - valeur envoyée nulle ou vide.", null);

        switch (valeur)
        {
            case Valeur.LEGACY:
                return LEGACY;

            case Valeur.MOBILECENTER:
                return MOBILECENTER;

            default:
                throw new IllegalArgumentException("model.enums.InstanceSonar.from - valeur non gérée : " + valeur);
        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @JsonValue
    public String getValeur()
    {
        return valeur;
    }

    /*---------- CLASSE INTERNE ----------*/

    private static final class Valeur
    {
        private static final String LEGACY = "legacy";
        private static final String MOBILECENTER = "mobile";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.InstanceSonar#Valeur");
        }
    }
}

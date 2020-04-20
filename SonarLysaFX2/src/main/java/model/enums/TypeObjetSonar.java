package model.enums;

/**
 * Enumeration de tous les types d'objets utilisés dans SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public enum TypeObjetSonar
{
    /*---------- ATTRIBUTS ----------*/

    APPLI(Valeur.APPLI),
    SUBPROJECT(Valeur.SUBPROJECT),
    DIRECTORY(Valeur.DIRECTORY),
    FILE(Valeur.FILE),
    PORTFOLIO(Valeur.PORTFOLIO),
    PROJECT(Valeur.PROJECT),
    TESTFILE(Valeur.TESTFILE);

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    TypeObjetSonar(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static TypeObjetSonar from(String valeur)
    {
        if (valeur == null || valeur.isEmpty())
            throw new IllegalArgumentException("model.enums.TypeObjetSonar.from - valeur envoyée nulle ou vide.", null);

        switch (valeur)
        {
            case Valeur.APPLI:
                return APPLI;

            case Valeur.SUBPROJECT:
                return SUBPROJECT;

            case Valeur.DIRECTORY:
                return DIRECTORY;

            case Valeur.FILE:
                return FILE;

            case Valeur.PORTFOLIO2:
            case Valeur.PORTFOLIO:
                return PORTFOLIO;

            case Valeur.PROJECT:
                return PROJECT;

            case Valeur.TESTFILE:
                return TESTFILE;

            default:
                throw new IllegalArgumentException("model.enums.TypeObjetSonar.from - valeur non gérée : " + valeur, null);

        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getValeur()
    {
        return valeur;
    }

    /*---------- CLASSES PRIVEES ----------*/

    private static final class Valeur
    {
        private static final String APPLI = "APP";
        private static final String SUBPROJECT = "BRC";
        private static final String DIRECTORY = "DIR";
        private static final String FILE = "FIL";
        private static final String PORTFOLIO2 = "SVW";
        private static final String PORTFOLIO = "VW";
        private static final String PROJECT = "TRK";
        private static final String TESTFILE = "UTS";

        private Valeur()
        {
            throw new AssertionError("model.enums.TypeObjetSonar#Valeur - classe non instanciable");
        }
    }
}

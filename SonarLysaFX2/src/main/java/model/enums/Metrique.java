package model.enums;

/**
 * Regroupe les différents metriques utilisés dasn SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public enum Metrique
{
    /** Valeur du Quality Gate */
    QG(Valeur.QG),
    /** Pourcentage de duplication du code */
    DUPLICATION(Valeur.DUPLICATION),
    /** Nombre de défauts bloquants */
    BLOQUANT(Valeur.BLOQUANT),
    /** Nombre de défauts critiques */
    CRITIQUE(Valeur.CRITIQUE),
    /** Code application */
    APPLI(Valeur.APPLI),
    /** Code édition */
    EDITION(Valeur.EDITION),
    /** Nombre de vulnérabilités */
    VULNERABILITIES(Valeur.VULNERABILITIES),
    /** Nombre de bugs */
    BUGS(Valeur.BUGS),
    /** Nombre de lignes de code */
    LDC(Valeur.LDC),
    /** Indice de sécurité */
    SECURITY(Valeur.SECURITY),
    /** Pourcentage de lignes de code couvertes par des TUs */
    PCTCOVERLDC(Valeur.PCTCOVERLDC),
    /** Pourcentage de nouvelles lignes de code couvertes par des TUs */
    NEWPCTCOVERLDC(Valeur.NEWPCTCOVERLDC),
    /** Nombre de lignes du nouveau code à couvrir par des TUs*/
    NEWLDCTOCOVER(Valeur.NEWLDCTOCOVER),
    /** Nombre de lignes du nouveau code sans couverture */
    NEWLDCNOCOVER(Valeur.NEWLDCNOCOVER);

    private final String valeur;

    Metrique(String valeur)
    {
        this.valeur = valeur;
    }

    public String getValeur()
    {
        return valeur;
    }

    public static Metrique from(String string)
    {
        if (string == null || string.isEmpty())
            throw new IllegalArgumentException("model.enums.Metrique.from - valeur envoyée nulle ou vide.", null);

        switch (string)
        {
            case Valeur.QG:
                return QG;

            case Valeur.DUPLICATION:
                return DUPLICATION;

            case Valeur.BLOQUANT:
                return BLOQUANT;

            case Valeur.CRITIQUE:
                return CRITIQUE;

            case Valeur.APPLI:
                return APPLI;

            case Valeur.EDITION:
                return EDITION;

            case Valeur.VULNERABILITIES:
                return VULNERABILITIES;

            case Valeur.BUGS:
                return BUGS;

            case Valeur.LDC:
                return LDC;

            case Valeur.PCTCOVERLDC:
                return PCTCOVERLDC;

            case Valeur.NEWPCTCOVERLDC:
                return NEWPCTCOVERLDC;

            case Valeur.NEWLDCTOCOVER:
                return NEWLDCTOCOVER;

            case Valeur.NEWLDCNOCOVER:
                return NEWLDCNOCOVER;

            case Valeur.SECURITY:
                return SECURITY;

            default:
                throw new IllegalArgumentException("model.enums.Metrique.from - valeur non gérée : " + string, null);
        }

    }

    private static final class Valeur
    {
        private static final String QG = "alert_status";
        private static final String DUPLICATION = "duplicated_lines_density";
        private static final String BLOQUANT = "blocker_violations";
        private static final String CRITIQUE = "critical_violations";
        private static final String APPLI = "application";
        private static final String EDITION = "edition";
        private static final String VULNERABILITIES = "vulnerabilities";
        private static final String BUGS = "bugs";
        private static final String LDC = "ncloc";
        private static final String PCTCOVERLDC = "line_coverage";
        private static final String NEWPCTCOVERLDC = "new_line_coverage";
        private static final String NEWLDCTOCOVER = "new_lines_to_cover";
        private static final String NEWLDCNOCOVER = "new_uncovered_lines";
        private static final String SECURITY = "security_rating";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeMetrique$Valeur");
        }
    }
}

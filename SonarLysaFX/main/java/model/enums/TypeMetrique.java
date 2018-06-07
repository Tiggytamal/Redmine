package model.enums;

/**
 * Regroupe les diff�rents m�ttriques utilis�s dasn SonarQube
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public enum TypeMetrique 
{
    LOT(Valeur.LOT), 
    QG(Valeur.QG), 
    DUPLICATION(Valeur.DUPLICATION), 
    BLOQUANT(Valeur.BLOQUANT), 
    CRITIQUE(Valeur.CRITIQUE), 
    APPLI(Valeur.APPLI), 
    EDITION(Valeur.EDITION),
    VULNERABILITIES(Valeur.VULNERABILITIES),
    BUGS(Valeur.BUGS);

    private final String valeur;

    private TypeMetrique(String valeur)
    {
        this.valeur = valeur;
    }

    @Override
    public String toString()
    {
        return valeur;
    }

    public static TypeMetrique from(String string)
    {
        switch (string)
        {
            case Valeur.LOT:
                return LOT;

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

            default:
                throw new IllegalArgumentException("model.enums.TypeMetrique inconnu : " + string, null);
        }

    }

    private static class Valeur
    {
        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeMetrique$Valeur");
        }

        private static final String LOT = "lot";
        private static final String QG = "alert_status";
        private static final String DUPLICATION = "duplicated_lines_density";
        private static final String BLOQUANT = "new_blocker_violations";
        private static final String CRITIQUE = "new_critical_violations";
        private static final String APPLI = "application";
        private static final String EDITION = "edition";
        private static final String VULNERABILITIES = "vulnerabilities";
        private static final String BUGS = "bugs";
    }
}
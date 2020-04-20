package model.enums;

/**
 * Colonnes du fichier des vulnerabilites
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum ColCompo implements ColW 
{
    NOM("Nom composant", "colNom"),
    NOUVEAU("Nouvelle version", "colNouv"),
    VERSION("Version", "colVersion"),
    QG("QualityGate", "colQG"),
    ANALYSE("Date dernière analyse", "colAnalyse"),
    NBRELIGNE("Nombre de ligne de code", "colNbreLigne"),
    COMPON1("Composant N-1", "colN1"),
    QGN1("Quality Gate N-1", "colQGN1");

    private final String valeur;
    private final String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    ColCompo(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    public String getNomCol()
    {
        return nomCol;
    }

    @Override
    public String getValeur()
    {
        return valeur;
    }
}

package model.enums;

/**
 * Colonnes du fichier des extractions des régles SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public enum ColRegle implements ColW
{
    /*---------- ATTRIBUTS ----------*/

    NOM("Nom", "colNom"),
    DESCRIPTION("Description", "colDesc"),
    KEY("Clef", "colKey"),
    LANGAGE("Langage", "colLang"),
    SEVERITE("Severite", "colSeverite"),
    TAGS("Tags", "colTags"),
    TYPE("Type", "colType"),
    ACTIVATION("Profils activés", "colActivation");

    private String valeur;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    ColRegle(String valeur, String nomCol)
    {
        this.valeur = valeur;
        this.nomCol = nomCol;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    public String getValeur()
    {
        return valeur;
    }

    @Override
    public String getNomCol()
    {
        return nomCol;
    }

}

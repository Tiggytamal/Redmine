package model.enums;

public enum TypeColNPC implements TypeColR 
{
    /*---------- ATTRIBUTS ----------*/

    NOM("Nom Projet", "colNom");

    private String valeur;
    private String nomCol;

    /*---------- CONSTRUCTEURS ----------*/

    private TypeColNPC(String valeur, String nomCol)
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

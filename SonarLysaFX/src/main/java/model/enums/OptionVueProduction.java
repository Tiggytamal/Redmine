package model.enums;

/**
 * Option pour la t�che CreerVueProductionTask
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum OptionVueProduction
{
    /*---------- ATTRIBUTS ----------*/

    ALL (""),
    DATASTAGE ("DataStage");
    
    private String titre;
    
    /*---------- CONSTRUCTEURS ----------*/

    private OptionVueProduction(String titre)
    {
        this.titre = titre;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getTitre()
    {
        return titre;
    }
}

package model.enums;

/**
 * Option pour la tâche CreerVueProductionTask
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum OptionPortfolioTrimestriel
{
    /*---------- ATTRIBUTS ----------*/

    ALL (""),
    DATASTAGE ("DataStage");
    
    private String titre;
    
    /*---------- CONSTRUCTEURS ----------*/

    OptionPortfolioTrimestriel(String titre)
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

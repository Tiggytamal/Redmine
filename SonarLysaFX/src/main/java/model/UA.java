package model;

import model.utilities.AbstractModele;

public class UA extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/
    
    private String codeUA;
    private String codeAppli;
    
    /*---------- CONSTRUCTEURS ----------*/

    UA() { }
    
    /*---------- METHODES PUBLIQUES ----------*/    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getCodeUA()
    {
        return codeUA;
    }

    public void setCodeUA(String codeUA)
    {
        this.codeUA = codeUA;
    }

    public String getCodeAppli()
    {
        return codeAppli;
    }

    public void setCodeAppli(String codeAppli)
    {
        this.codeAppli = codeAppli;
    }
}

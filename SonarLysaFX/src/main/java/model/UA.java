package model;

import model.interfaces.AbstractModele;

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
        return getString(codeUA);
    }

    public void setCodeUA(String codeUA)
    {
        this.codeUA = codeUA;
    }

    public String getCodeAppli()
    {
        return getString(codeAppli);
    }

    public void setCodeAppli(String codeAppli)
    {
        this.codeAppli = codeAppli;
    }
}

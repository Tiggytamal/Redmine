package model;

import java.io.Serializable;

/**
 * Classe de modèle pour les applications CATS
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class Application implements Modele, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String code;
    private boolean actif;
    private String libelle;
    private boolean open;
    private boolean mainFrame;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public boolean isMainFrame()
    {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame)
    {
        this.mainFrame = mainFrame;
    }
}
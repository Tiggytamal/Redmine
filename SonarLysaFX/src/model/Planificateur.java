package model;

import java.time.LocalTime;

/**
 * représente la configuration d'un planificateur
 * 
 * @author ETP8137 - Grégoire Mathon
 */
public class Planificateur
{
    /*---------- ATTRIBUTS ----------*/

    private boolean lundi;
    private boolean mardi;
    private boolean mercredi;
    private boolean jeudi;
    private boolean vendredi;
    private boolean active;
    private LocalTime heure;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * @return the lundi
     */
    public boolean isLundi()
    {
        return lundi;
    }

    /**
     * @param lundi
     *            the lundi to set
     */
    public void setLundi(boolean lundi)
    {
        this.lundi = lundi;
    }

    /**
     * @return the mardi
     */
    public boolean isMardi()
    {
        return mardi;
    }

    /**
     * @param mardi
     *            the mardi to set
     */
    public void setMardi(boolean mardi)
    {
        this.mardi = mardi;
    }

    /**
     * @return the mercredi
     */
    public boolean isMercredi()
    {
        return mercredi;
    }

    /**
     * @param mercredi
     *            the mercredi to set
     */
    public void setMercredi(boolean mercredi)
    {
        this.mercredi = mercredi;
    }

    /**
     * @return the jeudi
     */
    public boolean isJeudi()
    {
        return jeudi;
    }

    /**
     * @param jeudi
     *            the jeudi to set
     */
    public void setJeudi(boolean jeudi)
    {
        this.jeudi = jeudi;
    }

    /**
     * @return the vendredi
     */
    public boolean isVendredi()
    {
        return vendredi;
    }

    /**
     * @param vendredi
     *            the vendredi to set
     */
    public void setVendredi(boolean vendredi)
    {
        this.vendredi = vendredi;
    }

    /**
     * @return the heure
     */
    public LocalTime getHeure()
    {
        return heure;
    }

    /**
     * @param heure
     *            the heure to set
     */
    public void setHeure(LocalTime heure)
    {
        this.heure = heure;
    }

    /**
     * @return the active
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }
}

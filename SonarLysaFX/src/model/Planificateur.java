package model;

import static utilities.Statics.TODAY;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import utilities.adapter.LocalTimeAdapter;

/**
 * repr�sente la configuration d'un planificateur
 * 
 * @author ETP8137 - Gr�goire Mathon
 */
@XmlRootElement
public class Planificateur implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private boolean lundi;
    private boolean mardi;
    private boolean mercredi;
    private boolean jeudi;
    private boolean vendredi;
    private boolean active;
    private LocalTime heure;
    private List<String> annees;

    /*---------- CONSTRUCTEURS ----------*/

    Planificateur()
    {
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * @return the lundi
     */
    @XmlAttribute(name = "lundi")
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
    @XmlAttribute(name = "mardi")
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
    @XmlAttribute(name = "mercredi")
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
    @XmlAttribute(name = "jeudi")
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
    @XmlAttribute(name = "vendredi")
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
    @XmlAttribute(name = "heure")
    @XmlJavaTypeAdapter(value = LocalTimeAdapter.class)
    public LocalTime getHeure()
    {
        if (heure == null)
            heure = LocalTime.of(0, 0);
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
    @XmlAttribute(name = "active")
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

    @XmlAttribute(name = "annees", required = false)
    public List<String> getAnnees()
    {
        if (annees == null)
            annees = new ArrayList<>();
        if (!annees.contains(String.valueOf(TODAY.getYear())))
            annees.add(String.valueOf(TODAY.getYear()));
        return annees;
    }

    /**
     * Ajoute l'ann�e derni�re � la liste des ann�es
     * 
     * @return
     */
    public List<String> addLastYear()
    {
        if (annees == null)
            annees = new ArrayList<>();
        if (!annees.contains(String.valueOf(TODAY.getYear() - 1)))
            annees.add(String.valueOf(TODAY.getYear() - 1));
        return annees;
    }

    /**
     * Ajoute l'ann�e prochaine � la liste des ann�es
     * 
     * @return
     */
    public List<String> addNextYear()
    {
        if (annees == null)
            annees = new ArrayList<>();
        if (!annees.contains(String.valueOf(TODAY.getYear() + 1)))
            annees.add(String.valueOf(TODAY.getYear() + 1));
        return annees;
    }
}
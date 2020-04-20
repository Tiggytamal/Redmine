package model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import utilities.adapter.LocalDateAdapter;

/**
 * Classe de modèle de retour pour la recherche des composants mis en production sur un trimestre.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class KeyDateMEP extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String cle;
    private String nom;
    private LocalDate date;

    /*---------- CONSTRUCTEURS ----------*/

    public KeyDateMEP(String cle, String nom, LocalDate date)
    {
        this.cle = cle;
        this.nom = nom;
        this.date = date;
    }

    protected KeyDateMEP()
    {
        // Constructeur protected vide pour la persistance des données
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static KeyDateMEP build(String cle, String nom, LocalDate date)
    {
        return new KeyDateMEP(cle, nom, date);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getCle()
    {
        return getString(cle);
    }

    public void setCle(String cle)
    {
        this.cle = getString(cle);
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        if (date != null)
            this.date = date;
    }
}

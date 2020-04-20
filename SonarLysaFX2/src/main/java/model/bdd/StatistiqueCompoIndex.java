package model.bdd;

import java.time.LocalDate;

import model.enums.StatistiqueCompoEnum;

/**
 * Classe Index pour la classe modèle StatistiqueCompo
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class StatistiqueCompoIndex
{
    /*---------- ATTRIBUTS ----------*/

    private LocalDate date;
    private ComposantBase compo;
    private StatistiqueCompoEnum type;

    /*---------- CONSTRUCTEURS ----------*/

    public StatistiqueCompoIndex(LocalDate date, ComposantBase compo, StatistiqueCompoEnum type)
    {
        this.date = date;
        this.compo = compo;
        this.type = type;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public LocalDate getDate()
    {
        return date;
    }

    public ComposantBase getCompo()
    {
        return compo;
    }

    public StatistiqueCompoEnum getType()
    {
        return type;
    }
}

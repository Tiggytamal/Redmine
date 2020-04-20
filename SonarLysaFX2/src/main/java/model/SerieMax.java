package model;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import model.enums.StatistiqueEnum;

/**
 * Modèle représentant une série statistiques de points
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class SerieMax extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private Series<NumberAxis, NumberAxis> serie;
    private int valeurMax;
    private StatistiqueEnum type;

    /*---------- CONSTRUCTEURS ----------*/

    SerieMax()
    {}
    
    public SerieMax(Series<NumberAxis, NumberAxis> serie, int valeurMax, StatistiqueEnum type)
    {
        if (serie == null || type == null)
            throw new IllegalArgumentException("Paramètre null pour model.SerieMax");
        this.serie = serie;
        this.valeurMax = valeurMax;
        this.type = type;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public Series<NumberAxis, NumberAxis> getSerie()
    {
        return serie;
    }

    public int getValeurMax()
    {
        return valeurMax;
    }

    public StatistiqueEnum getType()
    {
        return type;
    }
}

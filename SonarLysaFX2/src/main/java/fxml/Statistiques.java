package fxml;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DaoFactory;
import dao.DaoStatistique;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.SerieMax;
import model.bdd.Statistique;
import model.enums.OptionTypeLong;
import model.enums.StatistiqueEnum;
import model.enums.TypeStatistique;
import utilities.AbstractToStringImpl;
import utilities.DateHelper;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Affichage des statistiques de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public final class Statistiques extends AbstractToStringImpl implements ImplShortCut
{
    /*---------- ATTRIBUTS ----------*/

    private static final int BORDURE = 20;

    @FXML
    private Button save;
    @FXML
    private HBox box;
    @FXML
    private VBox vbox;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<NumberAxis, NumberAxis> chart;
    @FXML
    private TextField borneSupp;
    @FXML
    private RadioButton radioSems;
    @FXML
    private RadioButton radioMois;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private ToggleGroup toggleGroup;

    /** Borne supérieure des données pour chaque série */
    private Map<String, Integer> valeursMax;

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        valeursMax = new HashMap<>();
        radioSems.setSelected(true);

        // Mise à jour borne supérieure axe x avec la date du jour
        xAxis.setUpperBound(dateTimeEnMilliSecondes(LocalDateTime.now()));
        xAxis.setTickUnit(Statics.MILLIJOUR * Statics.JOURSEMAINE);
        xAxis.setMinorTickCount(Statics.JOURSEMAINE);

        // Ajout des statistiques
        for (StatistiqueEnum stat : StatistiqueEnum.values())
        {
            if (stat.getType() == TypeStatistique.VALEUR)
                creerSerie(stat);
        }

        majBorneSuppY();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void implShortCut()
    {
        Utilities.creerRaccourciClavierBouton(save, box.getScene(), new KeyCodeCombination(KeyCode.ENTER));
        Utilities.creerRaccourciClavierBoutonCtrlS(save, box.getScene());
    }

    /*---------- METHODES FXML ----------*/

    @FXML
    private void save()
    {
        // Borne supérieure
        String borneSuppText = borneSupp.getText();
        if (borneSuppText != null && Statics.PATTERNNBRE.matcher(borneSuppText).matches())
        {
            int valeur = Integer.parseInt(borneSuppText);
            yAxis.setUpperBound(valeur);
            yAxis.setTickUnit(calculTickUnit(valeur));
        }

        // Gestion des radioboutons
        if (radioSems.isSelected())
        {
            xAxis.setTickUnit(Statics.MILLIJOUR * Statics.JOURSEMAINE);
            xAxis.setMinorTickVisible(true);
        }
        else if (radioMois.isSelected())
        {
            xAxis.setTickUnit(Statics.MILLIJOUR * Statics.JOURSMOIS);
            xAxis.setMinorTickVisible(false);
        }
        else
            throw new TechnicalException("control.view.StatistiquesViewControl.save - radiobouton inconnu ou non slectionné.");

        // Date de début
        if (dateDebut.getValue() != null)
            xAxis.setLowerBound(dateTimeEnMilliSecondes(dateDebut.getValue().atTime(0, 0)));

        // Date de fin
        if (dateFin.getValue() != null)
            xAxis.setUpperBound(dateTimeEnMilliSecondes(dateFin.getValue().atTime(0, 0)));
    }

    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void creerSerie(StatistiqueEnum statEnum)
    {
        // Récupération depuis la base de données des informations pour remplir la série.
        // On ne retient pas les week-ends
        List<Statistique> liste = ((DaoStatistique) DaoFactory.getMySQLDao(Statistique.class)).recupAllParType(statEnum);

        // ON saute les listes vides sans données
        if (liste.isEmpty())
            return;

        // Création de la série de données
        Series<NumberAxis, NumberAxis> serie = new Series<>();
        serie.setName(statEnum.getLabel());
        chart.getData().add(serie);

        int valeurMax = 0;

        for (Statistique stat : liste)
        {
            if (stat.getDate().getDayOfWeek() != DayOfWeek.SATURDAY && stat.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
            {
                double valeur = stat.getValeur();
                XYChart.Data data = new XYChart.Data(dateTimeEnMilliSecondes(stat.getDate().atTime(0, 0)), valeur);
                serie.getData().add(data);
                data.getNode().setOnMouseClicked(e -> Utilities.createAlert(formatTexteNode(data, statEnum)));

                // On recherche la valeur mixamale affichée par le graphique
                if (valeur > valeurMax)
                    valeurMax = (int) valeur;
            }
        }

        valeurMax = valeurMax + BORDURE;
        valeursMax.put(statEnum.getLabel(), valeurMax);
        ajouterBouton(new SerieMax(serie, valeurMax, statEnum), chart.getData());
    }

    private void ajouterBouton(SerieMax serieMax, List<Series<NumberAxis, NumberAxis>> data)
    {
        Button bouton = new Button(serieMax.getType().getLabel());
        bouton.setOnAction(event -> {
            if (data.contains(serieMax.getSerie()))
            {
                data.remove(serieMax.getSerie());
                valeursMax.remove(serieMax.getType().getLabel());
            }
            else
            {
                data.add(serieMax.getSerie());
                valeursMax.put(serieMax.getType().getLabel(), serieMax.getValeurMax());
            }
            majBorneSuppY();
        });
        vbox.getChildren().add(bouton);
        Region region = new Region();
        region.setPrefHeight(10);
        vbox.getChildren().add(region);
    }

    /**
     * Met à jour la borne supérieure de l'axe Y, puis change le texte du TextField affichant celle-ci et met à jour la graduation.
     */
    private void majBorneSuppY()
    {
        valeursMax.values().stream().max((i1, i2) -> i1.compareTo(i2)).ifPresent(i -> { yAxis.setUpperBound(i); borneSupp.setText(String.valueOf(i)); yAxis.setTickUnit(calculTickUnit(i)); });
    }

    private String formatTexteNode(@SuppressWarnings("rawtypes") XYChart.Data data, StatistiqueEnum type)
    {
        return "Date : " + Instant.ofEpochMilli((long) data.getXValue()).atZone(ZoneId.systemDefault()).toLocalDate() + "\n" + type.getLabel() + " : " + ((Double) data.getYValue()).intValue();
    }

    private int calculTickUnit(int yAxisValeur)
    {
        return yAxisValeur / 10;
    }

    private long dateTimeEnMilliSecondes(LocalDateTime dateTime)
    {
        return DateHelper.convertToLong(dateTime, OptionTypeLong.MILLISECOND);
    }

    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSES PRIVEES ----------*/
}

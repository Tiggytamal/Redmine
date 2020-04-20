package junit.model;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import javafx.scene.chart.XYChart.Series;
import junit.AutoDisplayName;
import model.SerieMax;
import model.enums.StatistiqueEnum;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestSerieMax extends TestAbstractModele<SerieMax>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/   
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new SerieMax(null, 0, StatistiqueEnum.COMPOSKO));
        assertThat(e.getMessage()).isEqualTo("Paramètre null pour model.SerieMax");
        e = assertThrows(IllegalArgumentException.class, () -> new SerieMax(new Series<>(), 0, null));
        assertThat(e.getMessage()).isEqualTo("Paramètre null pour model.SerieMax");
        e = assertThrows(IllegalArgumentException.class, () -> new SerieMax(null, 0, null));
        assertThat(e.getMessage()).isEqualTo("Paramètre null pour model.SerieMax");
    }
    
    @Test
    public void testConstructor(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        SerieMax serie = new SerieMax(new Series<>(), 0, StatistiqueEnum.COMPOSKO);
        assertThat(serie.getSerie()).isNotNull();
        assertThat(serie.getValeurMax()).isEqualTo(0);
        assertThat(serie.getType()).isEqualTo(StatistiqueEnum.COMPOSKO);
    }
    
    @Test
    public void testGetSerie(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getSerie()).isNull();        
        initObjet();        
        assertThat(objetTest.getSerie()).isNotNull();
    }
    
    @Test
    public void testGetValeurMax(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        assertThat(objetTest.getValeurMax()).isEqualTo(0);        
        initObjet();
        assertThat(objetTest.getValeurMax()).isEqualTo(10);           
    }
    
    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        assertThat(objetTest.getType()).isNull();
        initObjet();
        assertThat(objetTest.getType()).isEqualTo(StatistiqueEnum.NEWLDCTUP);   
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    private void initObjet()
    {
        objetTest = new SerieMax(new Series<>(), 10, StatistiqueEnum.NEWLDCTUP);
    }
    
    /*---------- ACCESSEURS ----------*/
}

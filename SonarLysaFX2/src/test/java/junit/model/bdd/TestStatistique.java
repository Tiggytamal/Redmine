package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import model.bdd.Statistique;
import model.enums.StatistiqueEnum;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestStatistique extends TestAbstractBDDModele<Statistique, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = Statistique.build(today, StatistiqueEnum.COMPOSKO);
        
        assertThat(objetTest.getDate()).isEqualTo(today);
        assertThat(objetTest.getType()).isEqualTo(StatistiqueEnum.COMPOSKO);
    }
    
    @Test
    public void testBuild_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Statistique.build(null, StatistiqueEnum.COMPOSKO)); 
        assertThat(e.getMessage()).isEqualTo("model.bdd.Statistique.build - date et/ou type nul(s)");
        
        e = assertThrows(IllegalArgumentException.class, () -> Statistique.build(today, null));  
        assertThat(e.getMessage()).isEqualTo("model.bdd.Statistique.build - date et/ou type nul(s)");
        
        e = assertThrows(IllegalArgumentException.class, () -> Statistique.build(null, null));
        assertThat(e.getMessage()).isEqualTo("model.bdd.Statistique.build - date et/ou type nul(s)");
    }
    
    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getMapIndex()).isEmpty();
        objetTest = Statistique.build(today, StatistiqueEnum.COMPOSKO);
        assertThat(objetTest.getMapIndex()).isEqualTo(today.toString());
    }
    
    @Test
    public void testHashCode(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        
    }
    
    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        
    }
    
    @Test
    public void testIncrementerValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getValeur()).isEqualTo(0);
        objetTest.incrementerValeur();
        assertThat(objetTest.getValeur()).isEqualTo(1);
        assertThat(objetTest.incrementerValeur().getValeur()).isEqualTo(2);
    }
    
    @Test
    public void testGetDate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleLocalDate(testInfo, () -> objetTest.getDate(), d -> objetTest.setDate(d));
    }
    
    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testSimpleInteger(testInfo, () -> objetTest.getValeur(), i -> objetTest.setValeur(i));
    }
    
    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getType()).isNull();        
        objetTest.setType(StatistiqueEnum.COMPOSKO);
        assertThat(objetTest.getType()).isEqualTo(StatistiqueEnum.COMPOSKO);
        
        // Protection null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(StatistiqueEnum.COMPOSKO);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

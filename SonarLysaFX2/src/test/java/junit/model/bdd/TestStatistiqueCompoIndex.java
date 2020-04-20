package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.JunitBase;
import model.bdd.StatistiqueCompoIndex;
import model.enums.StatistiqueCompoEnum;

public class TestStatistiqueCompoIndex extends JunitBase<StatistiqueCompoIndex>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = new StatistiqueCompoIndex(today, null, StatistiqueCompoEnum.NEWLDCNOCOVER);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetDate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getDate()).isEqualTo(today);
    }
    
    @Test
    public void testGetCompo(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getCompo()).isNull();
    }
    
    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getType()).isEqualTo(StatistiqueCompoEnum.NEWLDCNOCOVER);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package junit.control.statistique;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.statistique.ControlStatistique;
import junit.JunitBase;

public class TestControlStatistique extends JunitBase<ControlStatistique>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        objetTest = new ControlStatistique();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testMajStatistiquesFichier(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        
    }
    
    @Test
    public void testMajStatistiquesDefauts(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

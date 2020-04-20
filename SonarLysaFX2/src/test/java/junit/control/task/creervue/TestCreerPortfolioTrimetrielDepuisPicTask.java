package junit.control.task.creervue;

import static com.google.common.truth.Truth.assertThat;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.rtc.ControlRTC;
import control.task.portfolio.CreerPortfolioTrimetrielDepuisPicTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.OptionInitAPI;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerPortfolioTrimetrielDepuisPicTask extends TestAbstractTask<CreerPortfolioTrimetrielDepuisPicTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    
    @Override
    public void initImpl() throws Exception
    {

        objetTest = new CreerPortfolioTrimetrielDepuisPicTask(new File(getClass().getResource(Statics.ROOT + "TepPic.xlsx").getFile()), LocalDate.of(2019, 6, 1));
        initAPI(CreerPortfolioTrimetrielDepuisPicTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        ControlRTC.build().connexionSimple();
        assertThat((boolean) invokeMethod(objetTest, "call")).isTrue();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

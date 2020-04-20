package junit.control.task.extraction;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.util.Collection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExtractRegles;
import control.task.extraction.ExtractionReglesTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.OptionInitAPI;
import model.rest.sonarapi.DetailRegle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestExtractionReglesTask extends TestAbstractTask<ExtractionReglesTask>
{

    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    protected void initImpl() throws Exception
    {
        objetTest = new ExtractionReglesTask(new File("d:\\testRegles.xlsx"));
        initAPI(ExtractionReglesTask.class, OptionInitAPI.NOMOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @SuppressWarnings("unchecked")
    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation Mock pour diminuer durée test
        api = Mockito.spy(api);
        Mockito.doReturn(new DetailRegle()).when(api).getDetailsRegle(Mockito.anyString());

        // préparation mock pour vérification appel controlExcel
        ControlExtractRegles mock = Mockito.mock(ControlExtractRegles.class);
        Mockito.when(mock.write()).thenReturn(true);
        setField("control", mock);

        // Appel méthode
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isTrue();

        // Vérification appel creationExctraction et sauvegarde
        Mockito.verify(mock, Mockito.times(1)).creationExtraction(Mockito.any(Collection.class), Mockito.any(ExtractionReglesTask.class));
        Mockito.verify(mock, Mockito.times(1)).write();
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

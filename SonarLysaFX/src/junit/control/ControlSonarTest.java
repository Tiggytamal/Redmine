package junit.control;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.ControlSonar;
import control.task.CreerVuePatrimoineTask;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import junit.TestUtils;

@RunWith (JfxRunner.class)
public class ControlSonarTest
{
    private ControlSonar handler;
    public static boolean deser = false;

    @Before
    public void init() throws InvalidFormatException, JAXBException, IOException, InterruptedException
    {
        // handler = new ControlSonar();
        handler = new ControlSonar("ETP8137", "28H02m89,;:!");
        deser = true;
    }

    @Test
    public void recupererLotsSonarQube() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        TestUtils.callPrivate("recupererLotsSonarQube", handler, null);
    }

    @Test
    public void creationHeader() throws InvalidFormatException, IOException
    {
        String codeUser;
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m89,;:!");
        codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());

        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());
        codeUser.length();
    }

    @Test
    public void recupererComposantsSonar() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        TestUtils.callPrivate("recupererComposantsSonar", handler, null);
    }

    @Test
    @TestInJfxThread
    public void majFichierSuiviExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.majFichierSuiviExcel();
    }

    @Test
    @TestInJfxThread
    public void majFichierSuiviExcelDataStage() throws InvalidFormatException, IOException, JAXBException
    {
        handler.majFichierSuiviExcelDataStage();
    }

    @Test
    @TestInJfxThread
    public void traitementSuiviExcelToutFichiers() throws InvalidFormatException, IOException
    {
        handler.traitementSuiviExcelToutFichiers();
    }

    @Test
    @TestInJfxThread
    public void creerVuePatrimoine() throws Exception
    {
        CreerVuePatrimoineTask task2 = new CreerVuePatrimoineTask("ETP8137", "28H02m89,;:!");
        Whitebox.invokeMethod(task2, "creerVuePatrimoine");
    }
}
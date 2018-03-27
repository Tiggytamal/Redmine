package junit.control;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import control.ControlSonar;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import junit.TestUtils;

@RunWith(JfxRunner.class)
public class ControlSonarTest
{
    private ControlSonar handler;
    public static boolean deser;
    
    @Before
    public void init() throws InvalidFormatException, JAXBException, IOException, InterruptedException
    {
        // handler = new ControlSonar();
        handler = new ControlSonar("ETP8137", "28H02m89,;:!");
        deser = false;
    }

    @Test
    public void recupererLotsSonarQube() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        TestUtils.callPrivate("recupererLotsSonarQube", handler, null);
    }

    @Test
    public void creerVueProduction() throws InvalidFormatException, IOException
    {
        handler.creerVueProduction(new File("d:\\Classeur1.xlsx"));
    }

    @Test
    public void majVues() throws InvalidFormatException, IOException
    {
        handler.majVues();
    }

    @Test
    public void testAppli()
    {
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
    public void creerVueParApplication()
    {
        handler.creerVueParApplication();
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
    public void creerVuesDatastage()
    {
        handler.creerVuesDatastage();
    }
    
    @Test
    public void creerVueCDM() throws InvalidFormatException, IOException
    {
        handler.creerVueCDM();
    }
    
    @Test
    public void creerVueCHC()
    {
        handler.creerVueCHC();
    }
    
    @Test
    public void controlerSonarQube()
    {
        handler.controlerSonarQube();
    }
    
    @Test
    public void creerVuePatrimoine()
    {
       handler.creerVuePatrimoine(); 
    }
}
package junit.control;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.SchedulerException;

import control.quartz.ControlJob;

/**
 * JUnit pour control.quartz.ControlJob
 * @author ETP137 - Gr�goire Mathon
 *
 */
public class ControlJobTest
{
    private ControlJob handler;

    @Before
    public void init() throws InvalidFormatException, JAXBException, IOException, SchedulerException
    {
        handler = new ControlJob();
    }

    @Test
    public void testCreationJobAnomaliesSonar() throws SchedulerException, InterruptedException
    {
        handler.creationJobAnomaliesSonar();
    }

    @Test
    public void testFermeturePlanificateur() throws SchedulerException, InterruptedException
    {
        handler.fermeturePlanificateur();
    }
}

package junit.control.task.maj;

import org.junit.jupiter.api.DisplayNameGeneration;

import control.task.maj.AssignerAnoTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAssignerAnoTask extends TestAbstractTask<AssignerAnoTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    protected void initImpl() throws Exception
    {
        objetTest = new AssignerAnoTask();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

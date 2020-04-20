package junit.control.task.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.rtc.ControlRTC;
import control.task.action.TraiterActionDefautQualiteTask;
import dao.DaoDefautQualite;
import junit.control.task.TestAbstractTask;
import model.bdd.DefautQualite;
import model.enums.ActionDq;

public class TestTraiterActionDefautQualiteTask extends TestAbstractTask<TraiterActionDefautQualiteTask>
{
    /*---------- ATTRIBUTS ----------*/

    private DefautQualite dq;
    private ControlRTC controlRTCMock;
    private DaoDefautQualite daoMock;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    protected void initImpl() throws Exception
    {
        initDataBase();
        dq = databaseXML.getDqs().get(0);
        controlRTCMock = Mockito.mock(ControlRTC.class);
        daoMock = Mockito.mock(DaoDefautQualite.class);
        initObjet(ActionDq.ABANDONNER);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testTraitementAction_ABANDONNER(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        initObjet(ActionDq.ABANDONNER);

        Whitebox.invokeMethod(objetTest, "traitementAction", dq);
    }

    @Test
    public void testTraitementAction_OBSOLETE(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        initObjet(ActionDq.OBSOLETE);
        
        Whitebox.invokeMethod(objetTest, "traitementAction", dq);
    }

    @Test
    public void testTraitementAction_CLOTURER(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        initObjet(ActionDq.CLOTURER);
        
        Whitebox.invokeMethod(objetTest, "traitementAction", dq);
    }

    @Test
    public void testTraitementAction_CREER(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        initObjet(ActionDq.CREER);
        
       Whitebox.invokeMethod(objetTest, "traitementAction", dq);
    }

    @Test
    public void testTraitementAction_RELANCER(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        initObjet(ActionDq.RELANCER);
        
        Whitebox.invokeMethod(objetTest, "traitementAction", dq);
    }

    @Test
    public void testTraitementAction_MAJ(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        initObjet(ActionDq.MAJ);
        
        Whitebox.invokeMethod(objetTest, "traitementAction", dq);
    }

    /*---------- METHODES PRIVEES ----------*/

    private void initObjet(ActionDq action) throws IllegalAccessException
    {
        objetTest = new TraiterActionDefautQualiteTask(action, dq);
        setField("dao", daoMock);
        setField("controlRTC", controlRTCMock);
    }

    /*---------- ACCESSEURS ----------*/
}

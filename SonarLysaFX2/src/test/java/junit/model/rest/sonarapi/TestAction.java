package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Action;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAction extends TestAbstractModele<Action>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testIsCreate(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isCreate(), b -> objetTest.setCreate(b));
    }

    @Test
    public void testIsEdit(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isEdit(), b -> objetTest.setEdit(b));
    }

    @Test
    public void testIsSetAsDefault(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isSetAsDefault(), b -> objetTest.setSetAsDefault(b));
    }

    @Test
    public void testIsCopy(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isCopy(), b -> objetTest.setCopy(b));
    }

    @Test
    public void testIsDelete(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isDelete(), b -> objetTest.setDelete(b));
    }

    @Test
    public void testIsAssociateProjects(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isAssociateProjects(), b -> objetTest.setAssociateProjects(b));
    }

    @Test
    public void testIsManageConditions(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isManageConditions(), b -> objetTest.setManageConditions(b));
    }

    @Test
    public void testIsRename(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isRename(), b -> objetTest.setRename(b));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

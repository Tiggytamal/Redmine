package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Paging;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestPaging extends TestAbstractModele<Paging>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetTotal(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getTotal(), i -> objetTest.setTotal(i));
    }

    @Test
    public void testGetPageIndex(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getPageIndex(), i -> objetTest.setPageIndex(i));
    }

    @Test
    public void testGetPageSize(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getPageSize(), i -> objetTest.setPageSize(i));
    }
}

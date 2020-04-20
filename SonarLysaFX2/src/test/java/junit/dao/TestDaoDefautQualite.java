package junit.dao;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.DaoDefautQualite;
import junit.AutoDisplayName;
import model.bdd.DefautQualite;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDaoDefautQualite extends TestAbstractDao<DaoDefautQualite, DefautQualite, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testReadAll(TestInfo testInfo)
    {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testResetTable(TestInfo testInfo)
    {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        // TODO Auto-generated method stub

    }

    @Test
    public void testCrees()
    {
        objetTest.crees(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 8, 31));
    }

    @Test
    public void testAbandonnees()
    {
        objetTest.abandonnees(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 8, 31));
    }

    @Test
    public void testObsoletes()
    {
        objetTest.obsoletes(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 8, 31));
    }

    @Test
    public void testCloses()
    {
        objetTest.closes(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 8, 31));
    }

    @Test
    public void testEnCours()
    {
        objetTest.enCours(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 8, 31));
    }

    @Test
    public void testEnCoursTotal()
    {
        objetTest.enCoursTotal();
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

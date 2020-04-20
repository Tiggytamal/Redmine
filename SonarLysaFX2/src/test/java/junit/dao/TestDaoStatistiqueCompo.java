package junit.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.DaoFactory;
import dao.DaoStatistiqueCompo;
import model.bdd.ComposantBase;
import model.bdd.StatistiqueCompo;
import model.bdd.StatistiqueCompoIndex;
import model.enums.StatistiqueCompoEnum;
import utilities.Statics;

public class TestDaoStatistiqueCompo extends TestAbstractDao<DaoStatistiqueCompo, StatistiqueCompo, StatistiqueCompoIndex>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void testReadAll(TestInfo testInfo)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void testResetTable(TestInfo testInfo)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {

    }

    @Test
    public void TestRecupEltParIndex(TestInfo testInfo)
    {
        ComposantBase compo = DaoFactory.getMySQLDao(ComposantBase.class).recupEltParIndex("fr.ca.cat.ihm:ua_boacontratacceptation_Buildmaster");
        StatistiqueCompo test = objetTest.recupEltParIndex(new StatistiqueCompoIndex(Statics.TODAY, compo, StatistiqueCompoEnum.NEWLDCTOCOVER));
        System.out.println(test);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import utilities.Statics;

public class TestProjetClarity extends AbstractTestModel<ProjetClarity>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetProjetClarityInconnu()
    {
        objetTest = ProjetClarity.getProjetClarityInconnu("projet");
        assertEquals("projet", objetTest.getCode());
        assertEquals(Statics.INCONNU, objetTest.getChefProjet());
        assertEquals(Statics.INCONNU, objetTest.getEdition());
        assertEquals(Statics.INCONNU, objetTest.getDirection());
        assertEquals(Statics.INCONNU, objetTest.getDepartement());
        assertEquals(Statics.INCONNU, objetTest.getService());
        assertEquals("Code Clarity inconnu du réferentiel", objetTest.getLibelleProjet());
        assertEquals(false, objetTest.isActif());
    }

    @Test
    public void testGetMapIndex()
    {
        assertEquals(objetTest.getCode(), objetTest.getMapIndex());
    }

    @Test
    public void testUpdate()
    {
        ProjetClarity chef = ProjetClarity.getProjetClarityInconnu("projet");
        chef.setActif(true);
        chef.setChefProjet("chef");
        chef.setEdition("ed");
        chef.setDirection("dir");
        chef.setDepartement("dep");
        chef.setService("serv");
        chef.setLibelleProjet("lib");

        objetTest.update(chef);
        assertEquals(Statics.EMPTY, objetTest.getCode());
        assertEquals(true, objetTest.isActif());
        assertEquals("chef", objetTest.getChefProjet());
        assertEquals("ed", objetTest.getEdition());
        assertEquals("dir", objetTest.getDirection());
        assertEquals("dep", objetTest.getDepartement());
        assertEquals("serv", objetTest.getService());
        assertEquals("lib", objetTest.getLibelleProjet());
    }

    @Test
    public void testIsActif()
    {
        // test valeur vide ou nulle
        assertFalse(objetTest.isActif());

        // Test setter et getter
        objetTest.setActif(true);
        assertTrue(objetTest.isActif());
    }

    @Test
    public void testGetCodeClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCode());

        // Test setter et getter
        String codeClarity = "codeClarity";
        objetTest.setCode(codeClarity);
        assertEquals(codeClarity, objetTest.getCode());
    }

    @Test
    public void testGetLibelleProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLibelleProjet());

        // Test setter et getter
        String libelleProjet = "libelleProjet";
        objetTest.setLibelleProjet(libelleProjet);
        assertEquals(libelleProjet, objetTest.getLibelleProjet());
    }

    @Test
    public void testGetChefProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getChefProjet());

        // Test setter et getter
        String chefProjet = "chefProjet";
        objetTest.setChefProjet(chefProjet);
        assertEquals(chefProjet, objetTest.getChefProjet());
    }

    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getEdition());

        // Test setter et getter
        String edition = "edition";
        objetTest.setEdition(edition);
        assertEquals(edition, objetTest.getEdition());
    }

    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getDirection());

        // Test setter et getter
        String direction = "direction";
        objetTest.setDirection(direction);
        assertEquals(direction, objetTest.getDirection());
    }

    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getDepartement());

        // Test setter et getter
        String departement = "departement";
        objetTest.setDepartement(departement);
        assertEquals(departement, objetTest.getDepartement());
    }

    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getService());

        // Test setter et getter
        String service = "service";
        objetTest.setService(service);
        assertEquals(service, objetTest.getService());
    }

    @Test
    public void testGetChefService()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getChefService());

        // Test setter et getter
        ChefService chef = ChefService.getChefServiceInconnu("inco");
        objetTest.setChefService(chef);
        assertEquals(chef, objetTest.getChefService());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

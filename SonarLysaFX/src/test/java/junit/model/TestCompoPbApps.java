package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.CompoPbApps;

public class TestCompoPbApps extends AbstractTestModel<CompoPbApps>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetCodeComposant()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCodeComposant());

        // Test setter et getter
        String compo = "Composant";
        handler.setCodeComposant(compo);
        assertEquals(compo, handler.getCodeComposant());
    }

    @Test
    public void testGetCodeAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCodeAppli());

        // Test setter et getter
        String appli = "Appli";
        handler.setCodeAppli(appli);
        assertEquals(appli, handler.getCodeAppli());
    }

    @Test
    public void testGetCpiLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCpiLot());

        // Test setter et getter
        String cpi = "Cpi";
        handler.setCpiLot(cpi);
        assertEquals(cpi, handler.getCpiLot());
    }

    @Test
    public void testGetDepart()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getDepart());

        // Test setter et getter
        String depart = "Departement";
        handler.setDepart(depart);
        assertEquals(depart, handler.getDepart());
    }

    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getService());

        // Test setter et getter
        String service = "Service";
        handler.setService(service);
        assertEquals(service, handler.getService());
    }

    @Test
    public void testGetChefService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getChefService());

        // Test setter et getter
        String chefServ = "Chef de Service";
        handler.setChefService(chefServ);
        assertEquals(chefServ, handler.getChefService());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

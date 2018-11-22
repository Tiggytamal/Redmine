package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.CompoPbApps;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import model.enums.EtatAppli;
import utilities.Statics;

public class TestCompoPbApps extends AbstractTestModel<CompoPbApps>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testMajDepuisClarity()
    {
        // Prépartion données en entrée
        ProjetClarity pc = ProjetClarity.getProjetClarityInconnu("code");
        pc.setDepartement("depart.");
        pc.setService("serv.");
        
        // Test avant maj
        assertEquals(Statics.EMPTY, objetTest.getDepart());
        assertEquals(Statics.EMPTY, objetTest.getService());
        assertEquals(Statics.EMPTY, objetTest.getChefService());
        
        // Test avec objet null
        objetTest.majDepuisClarity(null);
        
        // Contrôle
        assertEquals(Statics.EMPTY, objetTest.getDepart());
        assertEquals(Statics.EMPTY, objetTest.getService());
        assertEquals(Statics.EMPTY, objetTest.getChefService());
        
        // Test avec chef de service null 
        objetTest.majDepuisClarity(pc);
        
        // Contrôle
        assertEquals("depart.", objetTest.getDepart());
        assertEquals("serv.", objetTest.getService());
        assertEquals(Statics.INCONNU, objetTest.getChefService());
        
        // Test avec chef de service connu
        pc.setChefService(ChefService.getChefServiceInconnu("serv."));
        objetTest.majDepuisClarity(pc);
        
        assertEquals("depart.", objetTest.getDepart());
        assertEquals("serv.", objetTest.getService());
        assertEquals("Chef de Service inconnu", objetTest.getChefService());
        
        
    }
    @Test
    public void testGetCodeComposant()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCodeComposant());

        // Test setter et getter
        String compo = "Composant";
        objetTest.setCodeComposant(compo);
        assertEquals(compo, objetTest.getCodeComposant());
    }

    @Test
    public void testGetCodeAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCodeAppli());

        // Test setter et getter
        String appli = "Appli";
        objetTest.setCodeAppli(appli);
        assertEquals(appli, objetTest.getCodeAppli());
    }

    @Test
    public void testGetCpiLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCpiLot());

        // Test setter et getter
        String cpi = "Cpi";
        objetTest.setCpiLot(cpi);
        assertEquals(cpi, objetTest.getCpiLot());
    }

    @Test
    public void testGetDepart()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getDepart());

        // Test setter et getter
        String depart = "Departement";
        objetTest.setDepart(depart);
        assertEquals(depart, objetTest.getDepart());
    }

    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getService());

        // Test setter et getter
        String service = "Service";
        objetTest.setService(service);
        assertEquals(service, objetTest.getService());
    }

    @Test
    public void testGetChefService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getChefService());

        // Test setter et getter
        String chefServ = "Chef de Service";
        objetTest.setChefService(chefServ);
        assertEquals(chefServ, objetTest.getChefService());
    }
    
    @Test
    public void testGetLotRTC()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLotRTC());

        // Test setter et getter
        String chefServ = "Chef de Service";
        objetTest.setLotRTC(chefServ);
        assertEquals(chefServ, objetTest.getLotRTC());
    }
    
    @Test
    public void testGetEtatAppli()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getEtatAppli());

        // Test setter et getter
        objetTest.setEtatAppli(EtatAppli.KO);
        assertEquals(EtatAppli.KO, objetTest.getEtatAppli());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

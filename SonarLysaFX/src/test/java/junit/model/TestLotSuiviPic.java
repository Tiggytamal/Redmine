package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.LotSuiviPic;

public class TestLotSuiviPic extends AbstractTestModel<LotSuiviPic>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLot());
        
        // Test setter et getter
        String lot = "lot";
        objetTest.setLot(lot);
        assertEquals(lot, objetTest.getLot());       
    }
    
    @Test
    public void testGetLibelle()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLibelle());
        
        // Test setter et getter
        String libelle = "libelle";
        objetTest.setLibelle(libelle);
        assertEquals(libelle, objetTest.getLibelle());       
    }
    
    @Test
    public void testGetProjetClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getProjetClarity());
        
        // Test setter et getter
        String projetClarity = "projetClarity";
        objetTest.setProjetClarity(projetClarity);
        assertEquals(projetClarity, objetTest.getProjetClarity());       
    }
    
    @Test
    public void testGetCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCpiProjet());
        
        // Test setter et getter
        String cpiProjet = "cpiProjet";
        objetTest.setCpiProjet(cpiProjet);
        assertEquals(cpiProjet, objetTest.getCpiProjet());       
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
    public void testGetNbreComposants()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getNbreComposants());
        
        // Test setter et getter
        int nbreComposants = 10;
        objetTest.setNbreComposants(nbreComposants);
        assertEquals(nbreComposants, objetTest.getNbreComposants());       
    }
    
    @Test
    public void testGetNbrePaquets()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getNbrePaquets());
        
        // Test setter et getter
        int nbrePaquets = 10;
        objetTest.setNbrePaquets(nbrePaquets);
        assertEquals(nbrePaquets, objetTest.getNbrePaquets());       
    }
    
    @Test
    public void testGetBuild()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getBuild());
        
        // Test setter et getter
        objetTest.setBuild(today);
        assertEquals(today, objetTest.getBuild());       
    }
    
    @Test
    public void testGetDevtu()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getDevtu());
        
        // Test setter et getter
        objetTest.setDevtu(today);
        assertEquals(today, objetTest.getDevtu());       
    }
    
    @Test
    public void testGetTfon()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getTfon());
        
        // Test setter et getter
        objetTest.setTfon(today);
        assertEquals(today, objetTest.getTfon());       
    }
    
    @Test
    public void testGetVmoe()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getVmoe());
        
        // Test setter et getter
        objetTest.setVmoe(today);
        assertEquals(today, objetTest.getVmoe());       
    }
    
    @Test
    public void testGetVmoa()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getVmoa());
        
        // Test setter et getter
        objetTest.setVmoa(today);
        assertEquals(today, objetTest.getVmoa());       
    }
    
    @Test
    public void testGetLivraison()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getLivraison());
        
        // Test setter et getter
        objetTest.setLivraison(today);
        assertEquals(today, objetTest.getLivraison());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

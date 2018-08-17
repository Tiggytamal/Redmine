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
        assertEquals(EMPTY, handler.getLot());
        
        // Test setter et getter
        String lot = "lot";
        handler.setLot(lot);
        assertEquals(lot, handler.getLot());       
    }
    
    @Test
    public void testGetLibelle()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLibelle());
        
        // Test setter et getter
        String libelle = "libelle";
        handler.setLibelle(libelle);
        assertEquals(libelle, handler.getLibelle());       
    }
    
    @Test
    public void testGetProjetClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getProjetClarity());
        
        // Test setter et getter
        String projetClarity = "projetClarity";
        handler.setProjetClarity(projetClarity);
        assertEquals(projetClarity, handler.getProjetClarity());       
    }
    
    @Test
    public void testGetCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCpiProjet());
        
        // Test setter et getter
        String cpiProjet = "cpiProjet";
        handler.setCpiProjet(cpiProjet);
        assertEquals(cpiProjet, handler.getCpiProjet());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEdition());
        
        // Test setter et getter
        String edition = "edition";
        handler.setEdition(edition);
        assertEquals(edition, handler.getEdition());       
    }
    
    @Test
    public void testGetNbreComposants()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getNbreComposants());
        
        // Test setter et getter
        int nbreComposants = 10;
        handler.setNbreComposants(nbreComposants);
        assertEquals(nbreComposants, handler.getNbreComposants());       
    }
    
    @Test
    public void testGetNbrePaquets()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getNbrePaquets());
        
        // Test setter et getter
        int nbrePaquets = 10;
        handler.setNbrePaquets(nbrePaquets);
        assertEquals(nbrePaquets, handler.getNbrePaquets());       
    }
    
    @Test
    public void testGetBuild()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getBuild());
        
        // Test setter et getter
        handler.setBuild(today);
        assertEquals(today, handler.getBuild());       
    }
    
    @Test
    public void testGetDevtu()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDevtu());
        
        // Test setter et getter
        handler.setDevtu(today);
        assertEquals(today, handler.getDevtu());       
    }
    
    @Test
    public void testGetTfon()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getTfon());
        
        // Test setter et getter
        handler.setTfon(today);
        assertEquals(today, handler.getTfon());       
    }
    
    @Test
    public void testGetVmoe()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getVmoe());
        
        // Test setter et getter
        handler.setVmoe(today);
        assertEquals(today, handler.getVmoe());       
    }
    
    @Test
    public void testGetVmoa()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getVmoa());
        
        // Test setter et getter
        handler.setVmoa(today);
        assertEquals(today, handler.getVmoa());       
    }
    
    @Test
    public void testGetLivraison()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getLivraison());
        
        // Test setter et getter
        handler.setLivraison(today);
        assertEquals(today, handler.getLivraison());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

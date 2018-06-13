package junit.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import junit.JunitBase;
import model.LotSuiviPic;
import model.ModelFactory;

public class TestLotSuiviPic extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private LotSuiviPic lotSUiviPic;
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        lotSUiviPic = ModelFactory.getModel(LotSuiviPic.class);
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void getLot()
    {
        // test valeur vide ou nulle
        assertEquals("", lotSUiviPic.getLot());
        
        // Test setter et getter
        String lot = "lot";
        lotSUiviPic.setLot(lot);
        assertEquals(lot, lotSUiviPic.getLot());       
    }
    
    @Test
    public void getLibelle()
    {
        // test valeur vide ou nulle
        assertEquals("", lotSUiviPic.getLibelle());
        
        // Test setter et getter
        String libelle = "libelle";
        lotSUiviPic.setLibelle(libelle);
        assertEquals(libelle, lotSUiviPic.getLibelle());       
    }
    
    @Test
    public void getProjetClarity()
    {
        // test valeur vide ou nulle
        assertEquals("", lotSUiviPic.getProjetClarity());
        
        // Test setter et getter
        String projetClarity = "projetClarity";
        lotSUiviPic.setProjetClarity(projetClarity);
        assertEquals(projetClarity, lotSUiviPic.getProjetClarity());       
    }
    
    @Test
    public void getCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals("", lotSUiviPic.getCpiProjet());
        
        // Test setter et getter
        String cpiProjet = "cpiProjet";
        lotSUiviPic.setCpiProjet(cpiProjet);
        assertEquals(cpiProjet, lotSUiviPic.getCpiProjet());       
    }
    
    @Test
    public void getEdition()
    {
        // test valeur vide ou nulle
        assertEquals("", lotSUiviPic.getEdition());
        
        // Test setter et getter
        String edition = "edition";
        lotSUiviPic.setEdition(edition);
        assertEquals(edition, lotSUiviPic.getEdition());       
    }
    
    @Test
    public void getNbreComposants()
    {
        // test valeur vide ou nulle
        assertEquals(0, lotSUiviPic.getNbreComposants());
        
        // Test setter et getter
        int nbreComposants = 10;
        lotSUiviPic.setNbreComposants(nbreComposants);
        assertEquals(nbreComposants, lotSUiviPic.getNbreComposants());       
    }
    
    @Test
    public void getNbrePaquets()
    {
        // test valeur vide ou nulle
        assertEquals(0, lotSUiviPic.getNbrePaquets());
        
        // Test setter et getter
        int nbrePaquets = 10;
        lotSUiviPic.setNbrePaquets(nbrePaquets);
        assertEquals(nbrePaquets, lotSUiviPic.getNbrePaquets());       
    }
    
    @Test
    public void getBuild()
    {
        // test valeur vide ou nulle
        assertEquals(null, lotSUiviPic.getBuild());
        
        // Test setter et getter
        lotSUiviPic.setBuild(today);
        assertEquals(today, lotSUiviPic.getBuild());       
    }
    
    @Test
    public void getDevtu()
    {
        // test valeur vide ou nulle
        assertEquals(null, lotSUiviPic.getDevtu());
        
        // Test setter et getter
        lotSUiviPic.setDevtu(today);
        assertEquals(today, lotSUiviPic.getDevtu());       
    }
    
    @Test
    public void getTfon()
    {
        // test valeur vide ou nulle
        assertEquals(null, lotSUiviPic.getTfon());
        
        // Test setter et getter
        lotSUiviPic.setTfon(today);
        assertEquals(today, lotSUiviPic.getTfon());       
    }
    
    @Test
    public void getVmoe()
    {
        // test valeur vide ou nulle
        assertEquals(null, lotSUiviPic.getVmoe());
        
        // Test setter et getter
        lotSUiviPic.setVmoe(today);
        assertEquals(today, lotSUiviPic.getVmoe());       
    }
    
    @Test
    public void getVmoa()
    {
        // test valeur vide ou nulle
        assertEquals(null, lotSUiviPic.getVmoa());
        
        // Test setter et getter
        lotSUiviPic.setVmoa(today);
        assertEquals(today, lotSUiviPic.getVmoa());       
    }
    
    @Test
    public void getLivraison()
    {
        // test valeur vide ou nulle
        assertEquals(null, lotSUiviPic.getLivraison());
        
        // Test setter et getter
        lotSUiviPic.setLivraison(today);
        assertEquals(today, lotSUiviPic.getLivraison());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

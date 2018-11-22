package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.Vulnerabilite;

/**
 * Classe de test de la classe de modèle Vulnerabilite
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TestVulnerabilite extends AbstractTestModel<Vulnerabilite>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetSeverite()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getSeverite());
        
        // Test setter et getter
        String severite = "Sev";
        objetTest.setSeverite(severite);
        assertEquals(severite, objetTest.getSeverite());       
    }
    
    @Test
    public void testGetComposant()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getComposant());
        
        // Test setter et getter
        String compo = "Compo";
        objetTest.setComposant(compo);
        assertEquals(compo, objetTest.getComposant());       
    }
    
    @Test
    public void testGetStatus()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getStatus());
        
        // Test setter et getter
        String status = "Status";
        objetTest.setStatus(status);
        assertEquals(status, objetTest.getStatus());       
    }
    
    @Test
    public void testGetMessage()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getMessage());
        
        // Test setter et getter
        String message = "Message";
        objetTest.setMessage(message);
        assertEquals(message, objetTest.getMessage());       
    }
    
    @Test
    public void testGetDateCreation()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getDateCreation());
        
        // Test setter et getter
        String date = "10-12-2018";
        objetTest.setDateCreation(date);
        assertEquals(date, objetTest.getDateCreation());       
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLot());
        
        // Test setter et getter
        String lot = "123456";
        objetTest.setLot(lot);
        assertEquals(lot, objetTest.getLot());       
    }
    
    @Test
    public void testGetClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getClarity());
        
        // Test setter et getter
        String clarity = "SVRP_qsdfhjl";
        objetTest.setClarity(clarity);
        assertEquals(clarity, objetTest.getClarity());       
    }
    
    @Test
    public void testGetAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getAppli());
        
        // Test setter et getter
        String appli = "ABCD";
        objetTest.setAppli(appli);
        assertEquals(appli, objetTest.getAppli());       
    }
    
    @Test
    public void testGetLib()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLib());
        
        // Test setter et getter
        String lib = "Lib";
        objetTest.setLib(lib);
        assertEquals(lib, objetTest.getLib());       
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}

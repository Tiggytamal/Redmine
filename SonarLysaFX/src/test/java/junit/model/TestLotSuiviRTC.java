package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static utilities.Statics.EMPTY;

import java.time.LocalDate;

import org.junit.Test;

import model.LotSuiviRTC;
import model.enums.EtatLot;

public class TestLotSuiviRTC extends AbstractTestModel<LotSuiviRTC>
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
        String direction = "123456";
        handler.setLot(direction);
        assertEquals(direction, handler.getLot());       
    }
    
    @Test
    public void testGetLibelle()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLibelle());
        
        // Test setter et getter
        String string = "Libelle";
        handler.setLibelle(string);
        assertEquals(string, handler.getLibelle());       
    }
    
    @Test
    public void testGetProjetClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getProjetClarity());
        
        // Test setter et getter
        String string = "Projet";
        handler.setProjetClarity(string);
        assertEquals(string, handler.getProjetClarity());       
    }
    
    @Test
    public void testGetCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCpiProjet());
        
        // Test setter et getter
        String string = "CPI";
        handler.setCpiProjet(string);
        assertEquals(string, handler.getCpiProjet());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEdition());
        
        // Test setter et getter
        String string = "Edition";
        handler.setEdition(string);
        assertEquals(string, handler.getEdition());       
    }
    
    @Test
    public void testGetEtatLot()
    {
        // test valeur vide ou nulle
        assertNull(handler.getEtatLot());
        
        // Test setter et getter
        handler.setEtatLot(EtatLot.DEVTU);;
        assertEquals(EtatLot.DEVTU, handler.getEtatLot());       
    }
    
    @Test
    public void testGetProjetRTC()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getProjetRTC());
        
        // Test setter et getter
        String string = "Projet";
        handler.setProjetRTC(string);
        assertEquals(string, handler.getProjetRTC());       
    }
    
    @Test
    public void testGetDateMajEtat()
    {
        // test valeur vide ou nulle
        assertNull(handler.getDateMajEtat());
        
        // Test setter et getter
        LocalDate date = LocalDate.of(2018, 10, 10);
        handler.setDateMajEtat(date);
        assertEquals(date, handler.getDateMajEtat());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}

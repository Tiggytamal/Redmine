package junit.control.excel;

import java.util.List;

import org.junit.Test;

import control.excel.ControlProduits;
import model.bdd.Produit;
import model.enums.TypeColProduit;

public class TestControlProduits extends TestControlExcelRead<TypeColProduit, ControlProduits, List<Produit>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlProduits()
    {
        super(TypeColProduit.class, "projets_npc.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la premi�re colonne est utilis�e.
        testCalculIndiceColonnes(1);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(liste -> liste.size() == 20); 
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

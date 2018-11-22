package junit.control.excel;

import java.util.List;

import org.junit.Test;

import control.excel.ControlGroupeProjets;
import model.bdd.Produit;
import model.enums.TypeColProduit;

public class TestControlTypeProjets extends TestControlExcelRead<TypeColProduit, ControlGroupeProjets, List<Produit>>
{

    public TestControlTypeProjets()
    {
        super(TypeColProduit.class, "projets_npc.xlsx");
    }

    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne est utilisée
        testCalculIndiceColonnes(1);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 20);
    }
}

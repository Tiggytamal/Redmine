package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlTypeProjets;
import model.enums.TypeColNPC;

public class TestControlTypeProjets extends TestControlExcelRead<TypeColNPC, ControlTypeProjets, Map<String, String>>
{

    public TestControlTypeProjets()
    {
        super(TypeColNPC.class, "projets_npc.xlsx");
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

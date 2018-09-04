package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlTypeProjets;
import model.enums.TypeColNPC;

public class TestControlTypeProjets extends TestControlExcelRead<TypeColNPC, ControlTypeProjets, Map<String, String>>
{

    protected TestControlTypeProjets()
    {
        super(TypeColNPC.class, "projets npc.xlsx");
    }

    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne n'est pas utilisée
        testCalculIndiceColonnes(0);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 20);
    }
}

package junit.control.excel;

import java.util.List;

import org.junit.Test;

import control.excel.ControlGroupeProjets;
import model.bdd.GroupementProjet;
import model.enums.TypeColGrProjet;

public class TestControlTypeProjets extends TestControlExcelRead<TypeColGrProjet, ControlGroupeProjets, List<GroupementProjet>>
{

    public TestControlTypeProjets()
    {
        super(TypeColGrProjet.class, "projets_npc.xlsx");
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

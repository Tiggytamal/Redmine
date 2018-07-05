package junit.control.excel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import control.excel.ControlPic;
import control.sonar.SonarAPI;
import model.LotSuiviPic;
import model.enums.TypeColPic;
import model.sonarapi.Vue;

public class TestControlPicMEP extends TestControlExcel<TypeColPic, ControlPic, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/
    
    private SonarAPI api;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlPicMEP()
    {
        super(TypeColPic.class, "MEP_mars_2018.xlsx");
        api = SonarAPI.INSTANCE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la premi�re colonne ne sert pas.
        testCalculIndiceColonnes(0);
    }

    @Test
    public void testRecupLotsExcelPourMEP()
    {
        // Initialisation - r�cup�rer vues depuis Sonar
        Map<String, Vue> mapQube = new HashMap<>();
        List<Vue> views = api.getVues();
        for (Vue view : views)
        {
            if (view.getName().startsWith("Lot "))
                mapQube.put(view.getName().substring(4), view);
        }

        Map<LocalDate, List<Vue>> map = handler.recupLotsExcelPourMEP(mapQube);
        assertNotNull(map);
        assertTrue("Taille map != 1", map.size() == 1);
        assertTrue("Taille liste vues vide", !map.values().isEmpty());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRecupLotsExcelPourMEPException1()
    {
        // Appel avec map vide
        Map<String, Vue> mapQube = new HashMap<>();
        handler.recupLotsExcelPourMEP(mapQube);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testRecupLotsExcelPourMEPException2()
    {
        // Appel avec map nulle
        handler.recupLotsExcelPourMEP(null);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
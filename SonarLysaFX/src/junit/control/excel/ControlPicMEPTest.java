package junit.control.excel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.proprietesXML;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import control.excel.ControlPic;
import model.LotSuiviPic;
import model.enums.TypeColPic;
import model.enums.TypeParam;
import sonarapi.SonarAPI;
import sonarapi.model.Vue;

public class ControlPicMEPTest extends ControlExcelTest<TypeColPic, ControlPic, Map<String, LotSuiviPic>>
{
    /*---------- ATTRIBUTS ----------*/
    private SonarAPI api;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlPicMEPTest()
    {
        super(TypeColPic.class, "/resources/MEP_mars_2018.xlsx");
        api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), "ETP8137", "28H02m89,;:!");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void calculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne ne sert pas.
        calculIndiceColonnes(0);
    }

    @Test
    public void recupLotsExcelPourMEP() throws IOException
    {
        // Initialisation - récupérer vues depuis Sonar
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
    public void recupLotsExcelPourMEPException1() throws IOException
    {
        // Appel avec map vide
        Map<String, Vue> mapQube = new HashMap<>();
        handler.recupLotsExcelPourMEP(mapQube);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void recupLotsExcelPourMEPException2() throws IOException
    {
        // Appel avec map nulle
        handler.recupLotsExcelPourMEP(null);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

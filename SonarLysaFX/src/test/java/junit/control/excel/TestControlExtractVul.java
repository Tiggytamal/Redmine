package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.reflect.Whitebox.getField;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExtractVul;
import junit.JunitBase;
import model.Vulnerabilite;
import model.enums.TypeVulnerabilite;
import utilities.Statics;

public class TestControlExtractVul extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private ControlExtractVul handler;
    private Workbook wb;
    
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractVul() throws Exception
    {
        handler = new ControlExtractVul(new File(Statics.RESOURCESTEST + "extract.xlsx"));
    }
    
    @Before
    public void init() throws IOException, IllegalArgumentException, IllegalAccessException
    {

        wb = (Workbook) getField(handler.getClass(), "wb").get(handler);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testAjouterExtraction() throws IllegalArgumentException, IllegalAccessException
    {
        // Initialisation
        Vulnerabilite vul1 = new Vulnerabilite();
        vul1.setAppli("appli");
        vul1.setLot("12456");
        vul1.setComposant("composant");
        Vulnerabilite vul2 = new Vulnerabilite();
        vul2.setAppli("appli2");
        vul2.setLot("124567");
        vul2.setComposant("composant2");
        List<Vulnerabilite> liste = new ArrayList<>();
        liste.add(vul1);
        
        // Appel méthode avec liste différente
        handler.ajouterExtraction(liste, TypeVulnerabilite.OUVERTE);

        liste.add(vul2);
        handler.ajouterExtraction(liste, TypeVulnerabilite.RESOLUE);
        
        
        // Controle de la taille des feuilles
        assertEquals(1, wb.getSheet(TypeVulnerabilite.OUVERTE.getNomSheet()).getLastRowNum());
        assertEquals(2, wb.getSheet(TypeVulnerabilite.RESOLUE.getNomSheet()).getLastRowNum());
        
    }
    
    @Test
    public void testInitTitres()
    {
        // test pour chaque valeur de TypeVulnerabilite
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            Row row = wb.getSheet(type.getNomSheet()).getRow(0);
            assertNotNull(row);
            assertEquals(9, row.getLastCellNum());
        }
    }
    
    @Test
    public void testCreateWb() throws Exception
    {
        assertNotNull(wb);
        assertNotNull(Whitebox.getField(ControlExtractVul.class, "helper").get(handler));
    }
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test que l'on a bien la surcharge avec aucun plantage
        Whitebox.invokeMethod(handler, "calculIndiceColonnes");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

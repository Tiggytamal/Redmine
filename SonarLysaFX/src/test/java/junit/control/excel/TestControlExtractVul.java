package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.reflect.Whitebox.getField;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExtractVul;
import control.task.CreerExtractVulnerabiliteTask;
import model.Vulnerabilite;
import model.enums.TypeColVul;
import model.enums.TypeVulnerabilite;

public class TestControlExtractVul extends TestControlExcelWrite<TypeColVul, ControlExtractVul, List<Vulnerabilite>>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractVul() throws Exception
    {
        super(TypeColVul.class, "extractVul.xlsx");
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
        handler.ajouterExtraction(liste, TypeVulnerabilite.OUVERTE, new CreerExtractVulnerabiliteTask(file));

        liste.add(vul2);
        handler.ajouterExtraction(liste, TypeVulnerabilite.RESOLUE, new CreerExtractVulnerabiliteTask(file));
               
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
    
    @Test
    public void testInitEnum() throws IllegalAccessException
    {
        // test - énumération du bon Type
        assertEquals(TypeColVul.class, getField(ControlExtractVul.class, "enumeration").get(handler)); 
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
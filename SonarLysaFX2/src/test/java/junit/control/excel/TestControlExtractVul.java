package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import control.excel.ControlExtractVul;
import control.task.portfolio.CreerPortfolioCHCCDMTask;
import junit.AutoDisplayName;
import model.Vulnerabilite;
import model.enums.ColVul;
import model.enums.TypeVulnerabilite;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlExtractVul extends TestAbstractControlExcelWrite<ColVul, ControlExtractVul, List<Vulnerabilite>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractVul() throws Exception
    {
        super(ColVul.class, "extractVul.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAjouterExtraction(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

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

        // Appel methode avec liste différente
        objetTest.ajouterExtraction(liste, TypeVulnerabilite.OUVERTE, Mockito.mock(CreerPortfolioCHCCDMTask.class));

        liste.add(vul2);
        objetTest.ajouterExtraction(liste, TypeVulnerabilite.RESOLUE, Mockito.mock(CreerPortfolioCHCCDMTask.class));

        // Controle de la taille des feuilles
        assertThat(wb.getSheet(TypeVulnerabilite.OUVERTE.getNomSheet()).getLastRowNum()).isEqualTo(1);
        assertThat(wb.getSheet(TypeVulnerabilite.RESOLUE.getNomSheet()).getLastRowNum()).isEqualTo(2);
    }

    @Test
    @Override
    public void testInitTitres(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test pour chaque valeur de TypeVulnerabilite
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            Row row = wb.getSheet(type.getNomSheet()).getRow(0);
            assertThat(row).isNotNull();
            assertThat(row.getLastCellNum()).isEqualTo(9);
        }
    }

    @Test
    public void testInitEnum(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test - enumeration du bon Type
        assertThat(getField("enumeration")).isEqualTo(ColVul.class);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

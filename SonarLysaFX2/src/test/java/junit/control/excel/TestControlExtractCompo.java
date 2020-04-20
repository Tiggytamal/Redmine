package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.excel.ControlExtractCompo;
import junit.AutoDisplayName;
import model.bdd.ComposantBase;
import model.enums.ColCompo;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlExtractCompo extends TestAbstractControlExcelWrite<ColCompo, ControlExtractCompo, Map<String, List<ComposantBase>>>
{
    /*---------- ATTRIBUTS ----------*/

    private Sheet sheet;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractCompo()
    {
        super(ColCompo.class, "extractCompo.xlsx");
    }

    @Override
    protected void initOther()
    {
        sheet = wb.getSheet("Composants Sonar");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreerFichierExtraction(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation de la map de test
        initDataBase();
        Map<String, List<ComposantBase>> map = prepareMap();

        // Appel de la methode
        objetTest.creerFichierExtraction(map, null);

        // Test du nombre de lignes
        assertThat(sheet.getLastRowNum()).isEqualTo(38);
    }

    @Test
    @Override
    public void testInitTitres(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test feuille créée
        Sheet sheet = wb.getSheet("Composants Sonar");
        assertThat(sheet).isNotNull();

        // Test première ligne
        Row row = sheet.getRow(0);
        assertThat(row).isNotNull();

        // Test chaque cellule créée
        for (Cell cell : row)
        {
            assertThat(cell).isNotNull();
            assertThat(cell.getCellType()).isEqualTo(CellType.STRING);
            assertThat(cell.getStringCellValue()).isNotEmpty();
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    private Map<String, List<ComposantBase>> prepareMap()
    {
        List<ComposantBase> compos = databaseXML.getCompos();
        Map<String, List<ComposantBase>> retour = new HashMap<>();
        String NUMEROTATION = "^.*\\d\\d$";

        // Tri des composants - en premier les nouveaux puis N-1 et N-2
        compos.sort((o1, o2) -> {
            if (!o1.getNom().matches(NUMEROTATION) && !o2.getNom().matches(NUMEROTATION))
                return 0;
            if (!o1.getNom().matches(NUMEROTATION))
                return -1;
            if (!o2.getNom().matches(NUMEROTATION))
                return 1;
            return Integer.valueOf(o2.getNom().substring(o2.getNom().length() - 2, o2.getNom().length()))
                    .compareTo(Integer.valueOf(o1.getNom().substring(o1.getNom().length() - 2, o1.getNom().length())));
        });

        // Création de la regex pour retirer les numéros de version des composants
        Pattern pattern = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

        // Remplissage de la map par nom de composant
        for (ComposantBase compo : compos)
        {
            // On ne prend pas en compte les branches non master dans le traitement
            if (!"master".equals(compo.getBranche()))
                continue;

            Matcher matcher = pattern.matcher(compo.getNom());

            if (matcher.find())
                retour.computeIfAbsent(matcher.group(0), l -> new ArrayList<>()).add(compo);
        }
        return retour;
    }
    /*---------- ACCESSEURS ----------*/

}

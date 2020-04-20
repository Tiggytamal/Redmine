package junit.fxml.factory;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.Dao;
import dao.DaoFactory;
import fxml.bdd.ComposantBDD;
import fxml.factory.ComposantFXMLTableRow;
import junit.AutoDisplayName;
import model.bdd.ComposantBase;
import model.enums.ActionC;
import model.fxml.ComposantFXML;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantFXMLTableRow extends TestAbstractTableRow<ComposantFXMLTableRow, ComposantFXML, String, ActionC, Dao<ComposantBase, String>, ComposantBase, ComposantBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String YELLOW = "-fx-background-color:lightyellow";
    private static final String ORANGE = "-fx-background-color:orange";

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new ComposantFXMLTableRow(new ComposantBDD(), DaoFactory.getMySQLDao(ComposantBase.class));
        fxml = new ComposantFXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testUpdateItem_Orange(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test style orange - composant en doublon
        fxml.setDoublon(true);
        fxml.setVersion("1.0.0");
        fxml.setVersionMax("1.0.0");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(ORANGE);

        // Test style non orange
        fxml.setDoublon(false);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    
    @Test
    public void testUpdateItem_Jaune(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test style jaune - composant dans version non terminale
        fxml.setVersion("0.0.9");
        fxml.setVersionMax("1.0.0");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(YELLOW);

        // Test style non jaune
        fxml.setVersion("1.0.0");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

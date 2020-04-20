package junit.fxml.factory;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.Dao;
import dao.DaoFactory;
import fxml.bdd.ComposantPlanteBDD;
import fxml.factory.ComposantPlanteFXMLTableRow;
import junit.AutoDisplayName;
import model.bdd.ComposantErreur;
import model.enums.ActionCp;
import model.fxml.ComposantPlanteFXML;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantPlanteFXMLTableRow extends TestAbstractTableRow<ComposantPlanteFXMLTableRow, ComposantPlanteFXML, String, ActionCp, Dao<ComposantErreur, String>, ComposantErreur, ComposantPlanteBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String GREEN = "-fx-background-color:lightgreen";
    private static final String YELLOW = "-fx-background-color:lightyellow";

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new ComposantPlanteFXMLTableRow(new ComposantPlanteBDD(), DaoFactory.getMySQLDao(ComposantErreur.class));
        fxml = new ComposantPlanteFXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testUpdateItem(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test style vert - Composant corrigé
        fxml.setExiste(true);
        fxml.setAPurger(false);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(GREEN);

        // Test style non vert
        fxml.setExiste(false);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();

        // Test style jaune - composant à purger
        fxml.setExiste(true);
        fxml.setAPurger(true);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(YELLOW);

        // Test non jaune
        fxml.setExiste(false);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

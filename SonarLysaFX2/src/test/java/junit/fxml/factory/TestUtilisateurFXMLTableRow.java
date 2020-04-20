package junit.fxml.factory;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.Dao;
import dao.DaoFactory;
import fxml.bdd.UtilisateurBDD;
import fxml.factory.UtilisateurFXMLTableRow;
import junit.AutoDisplayName;
import model.bdd.Utilisateur;
import model.enums.ActionU;
import model.fxml.UtilisateurFXML;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestUtilisateurFXMLTableRow extends TestAbstractTableRow<UtilisateurFXMLTableRow, UtilisateurFXML, String, ActionU, Dao<Utilisateur, String>, Utilisateur, UtilisateurBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String YELLOW = "-fx-background-color:lightyellow";

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new UtilisateurFXMLTableRow(new UtilisateurBDD(), DaoFactory.getMySQLDao(Utilisateur.class));
        fxml = new UtilisateurFXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testUpdateItem(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test style jaune - utilisateur désactivé
        fxml.setActive(false);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(YELLOW);

        // Test style non jaune
        fxml.setActive(true);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

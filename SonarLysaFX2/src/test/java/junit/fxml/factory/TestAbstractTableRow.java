package junit.fxml.factory;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit5.ApplicationExtension;

import dao.Dao;
import fxml.bdd.AbstractBDD;
import fxml.factory.FXMLTableRow;
import junit.JunitBase;
import model.bdd.AbstractBDDModele;
import model.enums.Action;
import model.fxml.AbstractFXMLModele;

@ExtendWith(ApplicationExtension.class)
public abstract class TestAbstractTableRow<T extends FXMLTableRow<U, I, A, D, M, C>, U extends AbstractFXMLModele<I>, I, A extends Action, D extends Dao<M, I>, M extends AbstractBDDModele<I>, C extends AbstractBDD<U, A, I>>
        extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    U fxml;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES ABSTRAITES ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testUpdateItem_Null_ou_vide(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test null ou vide
        appelUpdateItem(null, false);
        assertThat(objetTest.getStyle()).isEmpty();
        appelUpdateItem(null, true);
        assertThat(objetTest.getStyle()).isEmpty();
        appelUpdateItem(fxml, true);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    /*---------- METHODES PROTECTED ----------*/

    protected void appelUpdateItem(U u, boolean empty) throws Exception
    {
        Whitebox.invokeMethod(objetTest, "updateItem", u, empty);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package junit.fxml.node;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import fxml.node.ParamBoolView;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.ParamBool;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParamBooleanView extends TestAbstractFXML<ParamBoolView>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        // Pas besoin d'initialisation
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstrutor(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        ParamBoolView view = new ParamBoolView(ParamBool.FICHIERPICAUTO, true);
        assertThat(view.getType()).isEqualTo(ParamBool.FICHIERPICAUTO);
        assertThat(view.getField().isSelected()).isTrue();

        view = new ParamBoolView(ParamBool.FICHIERPICAUTO, null);
        assertThat(view.getType()).isEqualTo(ParamBool.FICHIERPICAUTO);
        assertThat(view.getField().isSelected()).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

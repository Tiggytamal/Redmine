package junit.fxml.node;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import fxml.node.ParamView;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.Param;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParamView extends TestAbstractFXML<ParamView>
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
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    public void testParamView(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String texte = "a";
        ParamView view = new ParamView(Param.FILTRECOBOL, texte);
        assertThat(view.getType()).isEqualTo(Param.FILTRECOBOL);
        assertThat(view.getField().getText()).isEqualTo(texte);

        view = new ParamView(Param.FILTRECOBOL, null);
        assertThat(view.getType()).isEqualTo(Param.FILTRECOBOL);
        assertThat(view.getField().getText().length()).isEqualTo(0);
    }
}

package junit.fxml.node;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.google.common.truth.Correspondence;

import fxml.node.ColonneIndiceView;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.ColCompo;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColonneIndiceView extends TestAbstractFXML<ColonneIndiceView<ColCompo>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new ColonneIndiceView<ColCompo>(ColCompo.NOM, KEY, "1");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetText(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter
        assertThat(objetTest.getType()).isEqualTo(ColCompo.NOM);
        assertThat(objetTest.getField().getText()).isEqualTo(KEY);
        assertThat(objetTest.getIndice().getText()).isEqualTo("1");
    }

    @Test
    public void testConstructor(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test protection null
        objetTest = new ColonneIndiceView<ColCompo>(ColCompo.NOM, null, null);
        assertThat(objetTest.getField().getText()).isEqualTo(EMPTY);
        assertThat(objetTest.getIndice().getText()).isEqualTo(EMPTY); 
        assertThat(objetTest.getStylesheets()).comparingElementsUsing( Correspondence.from((a, b) -> ((String)a).contains((String) b), "matches")).containsExactly(Statics.CSS);
        assertThat(objetTest.getStyleClass()).containsExactly("bgimage");
        assertThat(objetTest.getChildren()).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof"))
        .containsAnyOf(Label.class, TextField.class, Region.class, Separator.class);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

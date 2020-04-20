package junit.fxml.node;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.google.common.truth.Correspondence;

import fxml.node.ColonneView;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.ColEdition;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColonneView extends TestAbstractFXML<ColonneView<ColEdition>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new ColonneView<>(ColEdition.COMMENTAIRE, "a");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test ColonneView avec texte
        assertThat(objetTest.getField().getText()).isEqualTo("a");
        assertThat(objetTest.getType()).isEqualTo(ColEdition.COMMENTAIRE);

        // Test colonneView vide
        objetTest = new ColonneView<>(ColEdition.EDITION, null);
        assertThat(objetTest.getField().getText().length()).isEqualTo(0);
        assertThat(objetTest.getType()).isEqualTo(ColEdition.EDITION);

        // Test généraux
        assertThat(objetTest.getStylesheets()).comparingElementsUsing(Correspondence.from((a, b) -> ((String) a).contains((String) b), "matches")).containsExactly(Statics.CSS);
        assertThat(objetTest.getStyleClass()).containsExactly("bgimage");
        assertThat(objetTest.getChildren()).comparingElementsUsing(Correspondence.from((o, c) -> ((Class<?>) c).isInstance(o), "is instanceof")).containsAnyOf(Label.class, TextField.class,
                Separator.class);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

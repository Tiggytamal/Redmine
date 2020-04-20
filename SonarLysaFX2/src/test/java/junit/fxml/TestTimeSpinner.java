package junit.fxml;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.testfx.api.FxToolkit;

import fxml.node.TimeSpinner;
import javafx.scene.Scene;
import javafx.stage.Stage;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.Mode;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTimeSpinner extends TestAbstractFXML<TimeSpinner>
{
    /*---------- ATTRIBUTS ----------*/

    private Stage stage;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws TimeoutException
    {
        stage = FxToolkit.registerPrimaryStage();
        FxToolkit.setupFixture(() -> { 
            objetTest = new TimeSpinner(); 
            stage.setScene(new Scene(objetTest)); 
            stage.show(); 
            });
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testTimmeSpinner(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = new TimeSpinner(LocalTime.of(12, 12));
        assertThat(objetTest.valueProperty().get().getHour()).isEqualTo(12);
        assertThat(objetTest.valueProperty().get().getMinute()).isEqualTo(12);
    }

    @Test
    public void testGetMode(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getMode()).isAnyOf(Mode.HOURS, Mode.MINUTES);
    }

    @Test
    public void testIncrement(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Récupération horaire initial
        int hour = objetTest.valueProperty().get().getHour();
        int mins = objetTest.valueProperty().get().getMinute();

        // Test incrément et décrément
        objetTest.setMode(Mode.HOURS);
        objetTest.decrement();
        assertThat(objetTest.valueProperty().get().getHour()).isEqualTo(hour - 1);

        objetTest.setMode(Mode.HOURS);
        objetTest.increment();
        assertThat(objetTest.valueProperty().get().getHour()).isEqualTo(hour);

        objetTest.setMode(Mode.HOURS);
        objetTest.decrement(2);
        assertThat(objetTest.valueProperty().get().getHour()).isEqualTo(hour - 2);

        objetTest.setMode(Mode.HOURS);
        objetTest.increment(2);
        assertThat(objetTest.valueProperty().get().getHour()).isEqualTo(hour);

        objetTest.setMode(Mode.MINUTES);
        objetTest.increment();
        assertThat(objetTest.valueProperty().get().getMinute()).isEqualTo(mins + 1);

        objetTest.setMode(Mode.MINUTES);
        objetTest.decrement();;
        assertThat(objetTest.valueProperty().get().getMinute()).isEqualTo(mins);

        objetTest.setMode(Mode.MINUTES);
        objetTest.increment(2);
        assertThat(objetTest.valueProperty().get().getMinute()).isEqualTo(mins + 2);

        objetTest.setMode(Mode.MINUTES);
        objetTest.decrement(2);
        assertThat(objetTest.valueProperty().get().getMinute()).isEqualTo(mins);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

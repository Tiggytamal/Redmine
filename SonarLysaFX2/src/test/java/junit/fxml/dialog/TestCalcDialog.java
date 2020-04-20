package junit.fxml.dialog;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.TextMatchers;

import dao.DaoDefautQualite;
import fxml.dialog.CalcDialog;
import fxml.dialog.VBoxCalc;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.Severity;
import utilities.FunctionalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCalcDialog extends TestAbstractFXML<CalcDialog>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        FxToolkit.setupFixture(() -> { objetTest = new CalcDialog(); objetTest.show(); });
    }

    @AfterEach
    public void close() throws TimeoutException
    {
        FxToolkit.setupFixture(() -> objetTest.close());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    void testConstructeur(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat((Node) lookup(robot, "ok")).isInstanceOf(Button.class);
        assertThat((Node) lookup(robot, "datePicker")).isInstanceOf(DatePicker.class);
        assertThat((Node) lookup(robot, "crees")).isInstanceOf(VBoxCalc.class);
        assertThat((Node) lookup(robot, "abandonnees")).isInstanceOf(VBoxCalc.class);
        assertThat((Node) lookup(robot, "obsoletes")).isInstanceOf(VBoxCalc.class);
        assertThat((Node) lookup(robot, "closes")).isInstanceOf(VBoxCalc.class);
        assertThat((Node) lookup(robot, "enCours")).isInstanceOf(VBoxCalc.class);
        assertThat((Node) lookup(robot, "enCoursTotal")).isInstanceOf(VBoxCalc.class);
    }

    @Test
    void testCalculer(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock du dao
        DaoDefautQualite mock = Mockito.mock(DaoDefautQualite.class);
        Mockito.when(mock.crees(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(150L);
        Mockito.when(mock.abandonnees(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(100L);
        Mockito.when(mock.abandonneesSecu(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(10L);
        Mockito.when(mock.obsoletes(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(80L);
        Mockito.when(mock.obsoletesSecu(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(8L);
        Mockito.when(mock.closes(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(60L);
        Mockito.when(mock.closesSecu(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(6L);
        Mockito.when(mock.enCours(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(40L);
        Mockito.when(mock.enCoursSecu(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(4L);

        setField("dao", mock);

        ((DatePicker) lookup(robot, "datePicker")).setValue(LocalDate.of(2019, 6, 1));
        robot.clickOn(TextMatchers.hasText("Calculer"));
        Assertions.assertThat(((VBoxCalc) getField("crees")).getValeur()).isEqualTo("150");
        Assertions.assertThat(((VBoxCalc) getField("abandonnees")).getValeur()).isEqualTo("100 - 10");
        Assertions.assertThat(((VBoxCalc) getField("obsoletes")).getValeur()).isEqualTo("80 - 8");
        Assertions.assertThat(((VBoxCalc) getField("closes")).getValeur()).isEqualTo("60 - 6");
        Assertions.assertThat(((VBoxCalc) getField("enCours")).getValeur()).isEqualTo("40 - 4");
    }

    @Test
    void testCalculer_Exception_Sans_date(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        FunctionalException e = assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "calculer"));
        assertThat(e.getMessage()).isEqualTo("Veuillez choisir le premier jour d'un mois pour effectuer les calculs.");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    @ParameterizedTest
    @ValueSource(ints =
    { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 })
    void testCalculer_Exception_Mauvaise_date(int jour, TestInfo testInfo, FxRobot robot) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        ((DatePicker) lookup(robot, "datePicker")).setValue(LocalDate.of(2019, 7, jour));

        FunctionalException e = assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "calculer"));
        assertThat(e.getMessage()).isEqualTo("Veuillez choisir le premier jour d'un mois pour effectuer les calculs.");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package junit.control.parsing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import control.parsing.ControlJSON;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.enums.InstanceSonar;
import model.enums.QG;
import model.enums.TypeBranche;
import model.parsing.ComposantJSON;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlJSON extends JunitBase<ControlJSON>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = new ControlJSON();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testParsingCompoJSON(TestInfo testInfo) throws JsonParseException, JsonMappingException, IOException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création objet test
        ComposantJSON test = objetTest.parsingCompoJSON(new File(JunitBase.class.getResource("/testJSON.json").getFile()));

        // Contrôles
        assertThat(test).isNotNull();
        assertThat(test.getBranche()).isNotNull();
        assertThat(test.getProjet()).isNotNull();
        assertThat(test.getNumeroLot()).isEqualTo(345042);
        assertThat(test.getIdAnalyse()).isEqualTo("AWnh9PsZ_D4z6XhLuql0");
        assertThat(test.getInstance()).isEqualTo(InstanceSonar.LEGACY);
        assertThat(test.getQualityGate()).isEqualTo(QG.ERROR);
        assertThat(test.getBranche().getNom()).isEqualTo("master");
        assertThat(test.getBranche().getType()).isEqualTo(TypeBranche.LONG);
        assertThat(test.getBranche().isPrincipal()).isTrue();
        assertThat(test.getProjet().getKey()).isEqualTo("fr.ca.cat.pic.plugins:build:9999");
        assertThat(test.getProjet().getNom()).isEqualTo("Composant Maven_Plugin 9999");
    }
    
    @Test
    public void testParsingCompoJSON_Exception(TestInfo testInfo) throws JsonParseException, JsonMappingException, IOException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Mock du mapper
        ObjectMapper mock = Mockito.mock(ObjectMapper.class);
        Mockito.when(mock.readValue(Mockito.any(File.class), Mockito.eq(ComposantJSON.class))).thenThrow(new IOException("Test"));
        Whitebox.getField(objetTest.getClass(), "mapper").set(objetTest, mock);

        // Test retour exception et valeur message
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.parsingCompoJSON(new File(JunitBase.class.getResource("/testJSON.json").getFile())));
        assertThat(e.getCause()).isInstanceOf(IOException.class);
        assertThat(e.getCause().getMessage()).isEqualTo("Test");
        assertThat(e.getMessage()).contains("Impossible de récupérer le fichier JSON : ");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

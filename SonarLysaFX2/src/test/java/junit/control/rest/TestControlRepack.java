package junit.control.rest;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.rest.ControlRepack;
import junit.AutoDisplayName;
import model.rest.repack.RepackREST;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlRepack extends TestAbstractControlRest<ControlRepack>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlRepack() throws Exception
    {
        super();
    }

    @Override
    @BeforeEach
    public void init() throws JAXBException
    {
        objetTest = new ControlRepack();
        initDataBase();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetRepacksComposant(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test composant avec Repack
        List<RepackREST> liste = objetTest.getRepacksComposant(databaseXML.getMapCompos().get("Composant SRVT_RestituerPerimetreClientAllege 14"));
        assertThat(liste).isNotNull();
        assertThat(liste).isNotEmpty();
        assertThat(liste.get(0)).isNotNull();
    }

    @Test
    public void testGetRepacksComposant_Sans_repack(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test composant sans repack
        List<RepackREST> liste = objetTest.getRepacksComposant(databaseXML.getMapCompos().get("Composant SRVT_RecupererListeOperationsCarte 14"));
        assertThat(liste).isNotNull();
        assertThat(liste).isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

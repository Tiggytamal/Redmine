package junit.control.parsing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.parsing.ControlXML;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.AbstractModele;
import model.enums.ColChefServ;
import model.enums.ColClarity;
import model.enums.ColCompo;
import model.enums.ColEdition;
import model.parsing.ProprietesPersoXML;
import model.parsing.ProprietesXML;
import model.parsing.XML;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlXML extends JunitBase<ControlXML>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init()
    {
        objetTest = new ControlXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupererXML(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test récupération proprieteXML avec test des HashMap
        ProprietesXML propriete = objetTest.recupererXML(ProprietesXML.class);
        assertThat(propriete).isNotNull();
        assertThat(propriete.getMapParams()).isNotNull();
        assertThat(propriete.getMapParamsBool()).isNotNull();
        assertThat(propriete.getMapParamsSpec()).isNotNull();
        assertThat(propriete.getMapPlans()).isNotNull();
        assertThat(propriete.getEnumMapColR(ColClarity.class)).isNotNull();
        assertThat(propriete.getEnumMapColR(ColClarity.class)).isNotEmpty();
        assertThat(propriete.getEnumMapColW(ColCompo.class)).isNotNull();
        assertThat(propriete.getEnumMapColW(ColCompo.class)).isNotEmpty();
        assertThat(propriete.getEnumMapColR(ColChefServ.class)).isNotNull();
        assertThat(propriete.getEnumMapColR(ColChefServ.class)).isNotEmpty();
        assertThat(propriete.getEnumMapColR(ColEdition.class)).isNotNull();
        assertThat(propriete.getEnumMapColR(ColEdition.class)).isNotEmpty();
    }

    @Test
    public void testRecupererXML_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test fichier null
        TechnicalException e1 = assertThrows(TechnicalException.class, () -> objetTest.recupererXML(TestX.class));
        assertThat(e1.getMessage()).isEqualTo("Impossible de trouver le fichier de paramètres : Nom du fichier null");

        // Test fichier vide
        TechnicalException e2 = assertThrows(TechnicalException.class, () -> objetTest.recupererXML(TestX2.class));
        assertThat(e2.getMessage()).isEqualTo("Impossible de trouver le fichier de paramètres : Nom du fichier vide");

        // Test erreur nom fichier
        TechnicalException e3 = assertThrows(TechnicalException.class, () -> objetTest.recupererXML(TestX3.class));
        assertThat(e3.getMessage()).isEqualTo("Impossible de trouver le fichier de paramètres : test.xml");
    }

    @Test
    public void testRecupererXMLParNom_Fichier_existant(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test récupération proprieteXML avec test des HashMap
        ProprietesPersoXML propriete = objetTest.recupererXMLPersoParNom("MATHON Gregoire");
        assertThat(propriete).isNotNull();
        assertThat(propriete.getParams()).isNotNull();
        assertThat(propriete.getParamsSpec()).isNotNull();
        assertThat(propriete.getParamsSpec()).isNotEmpty();
    }

    @Test
    public void testRecupererXMLParNom_Fichier_non_existant(TestInfo testInfo) throws IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        Files.deleteIfExists(new File(Statics.JARPATH + "\\\\test.xml").toPath());

        // Test récupération proprieteXML avec test des HashMap
        ProprietesPersoXML propriete = objetTest.recupererXMLPersoParNom("test");
        assertThat(propriete).isNotNull();
        assertThat(propriete.getParams()).isNotNull();
        assertThat(propriete.getParamsSpec()).isNotNull();
    }

    @Test
    public void testSaveXML(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test normal avec fichier de paramètres
        assertThat(objetTest.saveXML(Statics.proprietesXML)).isTrue();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSES PRIVEES -----------*/

    /**
     * Classe pour tester l'exception des fichiers XML
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     */
    @XmlRootElement
    public static class TestX extends AbstractModele implements XML
    {
        public TestX()
        {

        }

        @Override
        public File getFile()
        {
            return null;
        }

        @Override
        public String controleDonnees()
        {
            return null;
        }
    }

    /**
     * Classe pour tester l'exception des fichiers XML bis
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     */
    @XmlRootElement
    public static class TestX2 extends AbstractModele implements XML
    {
        public TestX2()
        {

        }

        @Override
        public File getFile()
        {
            return new File("");
        }

        @Override
        public String controleDonnees()
        {
            return null;
        }
    }

    /**
     * Classe pour tester l'exception des fichiers XML ter
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     */
    @XmlRootElement
    public static class TestX3 extends AbstractModele implements XML
    {
        public TestX3()
        {

        }

        @Override
        public File getFile()
        {
            return new File("test.xml");
        }

        @Override
        public String controleDonnees()
        {
            return null;
        }
    }
}

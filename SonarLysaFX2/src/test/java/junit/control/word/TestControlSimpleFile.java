package junit.control.word;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import control.word.ControlSimpleFile;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.bdd.ComposantBase;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlSimpleFile extends JunitBase<ControlSimpleFile>
{
    /*---------- ATTRIBUTS ----------*/

    private BufferedWriter writerMock;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = ControlSimpleFile.majFichierVersion();
        writerMock = (BufferedWriter) Mockito.spy(getField("writer"));
        setField("writer", writerMock);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @ParameterizedTest
    @ValueSource(strings = { "fichier1.txt", "fichier2.doc", "fichier3.rtf" })
    public void testControleur_new_File(String nomFichier, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel constructeur avec fichiers différents
        File file = new File(nomFichier);
        file.deleteOnExit();
        new ControlSimpleFile(file);

        // Contrôle des fichiers
        assertThat(file).isNotNull();
        assertThat(file.exists()).isTrue();
    }

    @Test
    public void testControleur_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel création avec erreur nom fichier
        File file = new File("('-_/?çè0.");
        file.deleteOnExit();

        // Contrôle bon renvoi de l'exception
        TechnicalException e = assertThrows(TechnicalException.class, () -> new ControlSimpleFile(file));
        assertThat(e.getMessage()).isEqualTo("control.word.ControlSimpleFile - Impossible de créer le fichier : " + file.getPath());
        assertThat(e.getCause()).isInstanceOf(IOException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = { "abs", "testeTexte124klm", "autreTest dsf dfesdf" })
    public void testAddTexte(String texte, TestInfo testInfo) throws IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.ajouterTexte(texte);

        Mockito.verify(writerMock, Mockito.times(1)).write(texte);
    }

    @Test
    public void testAddTexte_Exception(TestInfo testInfo) throws IllegalAccessException, IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Modification du mock du writer pour remonter une exception
        Mockito.doThrow(new IOException()).when(writerMock).write(Mockito.anyString());

        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.ajouterTexte("texte"));
        assertThat(e.getMessage()).contains("impossible d'ecrire dans fichier : ");
        assertThat(e.getCause()).isInstanceOf(IOException.class);
    }

    @Test
    public void testAjouterErreurCompo(TestInfo testInfo) throws JAXBException, IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        initDataBase();
        for (int i = 0; i < 10; i++)
        {
            ComposantBase compo = databaseXML.getCompos().get(i);

            // Appel méthode
            objetTest.ajouterErreurCompo(compo);

            // Contrôle
            Mockito.verify(writerMock, Mockito.times(1)).write(Mockito.contains(compo.getNom() + Statics.TIRET2 + compo.getBranche() + Statics.TIRET2 + compo.getVersion() + Statics.TIRET2 + compo.getVersionMax()));
        }
    }

    @Test
    public void testFermeture(TestInfo testInfo) throws IOException
    {
        LOGGER.debug(testInfo.getDisplayName());       
        
        // Test fermeture
        objetTest.fermeture();

        // Test remontée exception aprés fermeture writer
        assertThrows(IOException.class, () -> writerMock.flush());
    }
    
    @Test
    public void testFermeture_Exception(TestInfo testInfo) throws IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test exception fermeture
        Mockito.doThrow(new IOException()).when(writerMock).close();
                
        // Test fermeture
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.fermeture());
        assertThat(e.getMessage()).contains("impossible de fermer le fichier : ");
        assertThat(e.getCause()).isInstanceOf(IOException.class);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

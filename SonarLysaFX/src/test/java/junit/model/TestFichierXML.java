package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import junit.TestXML;
import model.FichiersXML;
import utilities.Statics;

public class TestFichierXML extends AbstractTestModel<FichiersXML> implements TestXML
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testGetFile()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getFile();
        assertNotNull(file);
        assertTrue(file.isFile());
        assertTrue(file.getPath().contains(Statics.JARPATH));
        assertEquals("fichiers.xml", file.getName());
    }

    @Test
    @Override
    public void testGetResource()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getResource();
        assertNotNull(file);
        assertTrue(file.isFile());
    }


    /*---------- METHODES PRIVEES ----------*/


    /*---------- ACCESSEURS ----------*/
}

package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.JunitBase;
import model.ModelFactory;
import model.ProprietesXML;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeColR;
import model.enums.TypeColSuivi;
import utilities.Statics;
import utilities.TechnicalException;

public class TestProprieteXML extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ProprietesXML handler;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        handler = ModelFactory.getModel(ProprietesXML.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testProprieteXML()
    {
        // Test initialisation des map à la construction       
        assertNotNull(handler.getMapParams());
        assertEquals(0, handler.getMapParams().size());
        assertNotNull(handler.getMapParamsBool());
        assertEquals(0, handler.getMapParamsBool().size());
        assertNotNull(handler.getMapPlans());
        assertEquals(0, handler.getMapPlans().size());     
    }
    
    
    @Test
    public void testControleDonnees()
    {
        // Initialisation. mock des méthode de contrôle
        
        // Test all true
    }
    
    @Test
    public void testGetEnumMap()
    {
        Map<TypeColSuivi, String> mapColsSuivi = handler.getEnumMap(TypeColSuivi.class); 
        assertNotNull(mapColsSuivi);
        assertEquals(0, mapColsSuivi.size());
        Map<TypeColClarity, String> mapColsClarity = handler.getEnumMap(TypeColClarity.class);
        assertNotNull(mapColsClarity);
        assertEquals(0, mapColsClarity.size());
        Map<TypeColApps, String> mapColsApps = handler.getEnumMap(TypeColApps.class);
        assertNotNull(mapColsApps);
        assertEquals(0, mapColsApps.size());
        Map<TypeColChefServ, String> mapColsChefServ = handler.getEnumMap(TypeColChefServ.class);
        assertNotNull(mapColsChefServ);
        assertEquals(0, mapColsChefServ.size());
        Map<TypeColEdition, String> mapColsEdition = handler.getEnumMap(TypeColEdition.class);
        assertNotNull(mapColsEdition);
        assertEquals(0, mapColsEdition.size());
        Map<TypeColPic, String> mapColsPic = handler.getEnumMap(TypeColPic.class);
        assertNotNull(mapColsPic);
        assertEquals(0, mapColsPic.size());
    }
    
    @Test (expected = TechnicalException.class)
    public void testGetEnumMapException()
    {
        handler.getEnumMap(TypeColTest.class);
    }
    
    @Test
    public void testGetMapColsInvert()
    {
        // Initialisation de la map ColSuivi
        Map<TypeColSuivi, String> mapColsSuivi = new HashMap<>();
        mapColsSuivi.put(TypeColSuivi.ANOMALIE, "key");
        handler.getEnumMap(TypeColSuivi.class).putAll(mapColsSuivi);
        
        
        
    }
    
    @Test
    public void testGetterPrive() throws Exception
    {
        testMapEnum("getMapColsSuivi");
        testMapEnum("getMapColsClarity");
        testMapEnum("getMapColsChefServ");
        testMapEnum("getMapColsPic");
        testMapEnum("getMapColsEdition");
        testMapEnum("getMapColsApps"); 
    }
    
    @Test
    public void testGetFile()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getFile();
        assertNotNull(file);
        assertTrue(file.isFile());
        assertTrue(file.getPath().contains(Statics.JARPATH));
        assertEquals("proprietes.xml", file.getName());
    }

    @Test
    public void testGetResource()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getResource();
        assertNotNull(file);
        assertTrue(file.isFile());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * permet d'appeler les getters privés des EnumMap
     * 
     * @param methode
     * @throws Exception
     */
    private <T> void testMapEnum(String methode) throws Exception
    {
        Map<T, String> map = Whitebox.invokeMethod(handler, methode);
        assertNotNull(map);
        assertEquals(0, map.size());
    }
    /*---------- ACCESSEURS ----------*/
    
    /*---------- CLASSES PRIVEES ----------*/
    
    /**
     * Enumération privée pour créer l'exception
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private enum TypeColTest implements Serializable, TypeColR
    {
        VIDE;

        @Override
        public String getValeur()
        {
            return null;
        }

        @Override
        public String getNomCol()
        {
            return null;
        }
        
    }
}

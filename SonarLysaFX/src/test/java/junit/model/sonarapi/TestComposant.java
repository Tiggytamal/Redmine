package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;

public class TestComposant
{
    /*---------- ATTRIBUTS ----------*/

    private Composant modele;
    private Composant modeleNull;
    private static final String ID = "id";
    private static final String KEY = "key";
    private static final String NOM = "nom";
    private static final String DESC = "description";
    private static final String QUALIFIER = "qualifier";
    private static final String LANGUAGE = "key";
    private static final String PATH = "key";
    private static final List<Metrique> METRIQUE = new ArrayList<>();
    private static final String UUID = "key";
    private static final boolean ENABLED = true;
    private static final String LONGNAME = "longName";
    private static final int PROJECTID = 123456;
    private static final int SUPROJECTID = 123456789;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Composant(ID, KEY, NOM, DESC, QUALIFIER, LANGUAGE, PATH, METRIQUE, UUID, ENABLED, LONGNAME, PROJECTID, SUPROJECTID);
        modeleNull = new Composant();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetMapMetriques() throws IllegalAccessException
    {
        // Test Liste vide
        Map<TypeMetrique, Metrique> mapVide = new EnumMap<>(TypeMetrique.class);
        
        // Contrôle que la liste est toujours au moins instanciée
        assertEquals(mapVide, modele.getMapMetriques());
        assertEquals(mapVide, modeleNull.getMapMetriques());
        
        // Test avec un objet dans la liste
        Metrique metrique = new Metrique(TypeMetrique.APPLI, "Appli");
        modele.getMetriques().add(metrique);
        
        // Contrôle que l'on récupère bien le métrique dans la map
        assertTrue(!modele.getMapMetriques().isEmpty());
        assertEquals(metrique, modele.getMapMetriques().get(TypeMetrique.APPLI));    
        
        // Contrôle que le map est vide si on rajoute un metrique sans clef d'énumération
        modele.getMetriques().clear();
        Whitebox.getField(Metrique.class, "type").set(metrique, null);
        modele.getMetriques().add(metrique);
        assertTrue(modele.getMapMetriques().isEmpty());
    }
    
    @Test
    public void testGetId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void testSetId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(ID);
        assertEquals(ID, modeleNull.getId());
    }
    
    @Test
    public void testGetKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
    }
    
    @Test
    public void testSetKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
      
    @Test
    public void testGetNom()
    {
        assertEquals(NOM, modele.getNom());
        assertNull(modeleNull.getNom());
    }
    
    @Test
    public void testSetNom()
    {
        modele.setNom(NEWVAL);
        assertEquals(NEWVAL, modele.getNom());
        modeleNull.setNom(NOM);
        assertEquals(NOM, modeleNull.getNom());
    }
    
    @Test
    public void testGetDescritpion()
    {
        assertEquals(DESC, modele.getDescritpion());
        assertNull(modeleNull.getDescritpion());
    }
    
    @Test
    public void testSetDescritpion()
    {
        modele.setDescritpion(NEWVAL);
        assertEquals(NEWVAL, modele.getDescritpion());
        modeleNull.setDescritpion(DESC);
        assertEquals(DESC, modeleNull.getDescritpion());
    }
    
    @Test
    public void testGetQualifier()
    {
        assertEquals(QUALIFIER, modele.getQualifier());
        assertNull(modeleNull.getQualifier());
    }
    
    @Test
    public void testSetQualifier()
    {
        modele.setQualifier(NEWVAL);
        assertEquals(NEWVAL, modele.getQualifier());
        modeleNull.setQualifier(QUALIFIER);
        assertEquals(QUALIFIER, modeleNull.getQualifier());
    }
    
    @Test
    public void testGetLangage()
    {
        assertEquals(LANGUAGE, modele.getLangage());
        assertNull(modeleNull.getLangage());
    }
    
    @Test
    public void testSetLangage()
    {
        modele.setLangage(NEWVAL);
        assertEquals(NEWVAL, modele.getLangage());
        modeleNull.setLangage(LANGUAGE);
        assertEquals(LANGUAGE, modeleNull.getLangage());
    }
    
    @Test
    public void testGetPath()
    {
        assertEquals(PATH, modele.getPath());
        assertNull(modeleNull.getPath());
    }
    
    @Test
    public void testSetPath()
    {
        modele.setPath(NEWVAL);
        assertEquals(NEWVAL, modele.getPath());
        modeleNull.setPath(PATH);
        assertEquals(PATH, modeleNull.getPath());
    }
    
    @Test
    public void testGetMetriques()
    {
        assertEquals(METRIQUE, modele.getMetriques());
        assertNotNull(modeleNull.getMetriques());
    }
    
    @Test
    public void testSetMetriques()
    {
        List<Metrique> liste = new ArrayList<>();
        modele.setMetriques(liste);
        assertEquals(liste, modele.getMetriques());
        modeleNull.setMetriques(liste);
        assertEquals(liste, modeleNull.getMetriques());
    }
    
    @Test
    public void testGetUuid()
    {
        assertEquals(UUID, modele.getUuid());
        assertNull(modeleNull.getUuid());
    }
    
    @Test
    public void testSetUuid()
    {
        modele.setUuid(NEWVAL);
        assertEquals(NEWVAL, modele.getUuid());
        modeleNull.setUuid(UUID);
        assertEquals(UUID, modeleNull.getUuid());
    }
        
    @Test
    public void testIsEnabled()
    {
        assertTrue(modele.isEnabled());
        assertFalse(modeleNull.isEnabled());
    }
    
    @Test
    public void testSetEnabled()
    {
        modele.setEnabled(false);
        assertFalse(modele.isEnabled());
        modeleNull.setEnabled(true);
        assertTrue(modeleNull.isEnabled());
    }
    
    @Test
    public void testGetLongName()
    {
        assertEquals(LONGNAME, modele.getLongName());
        assertNull(modeleNull.getLongName());
    }
    
    @Test
    public void testSetLongName()
    {
        modele.setLongName(NEWVAL);
        assertEquals(NEWVAL, modele.getLongName());
        modeleNull.setKey(LONGNAME);
        assertEquals(LONGNAME, modeleNull.getKey());
    }
    
    @Test
    public void testGetProjectId()
    {
        assertEquals(PROJECTID, modele.getProjectId());
        assertEquals(0, modeleNull.getProjectId());
    }
    
    @Test
    public void testSetProjectId()
    {
        modele.setProjectId(10);
        assertEquals(10, modele.getProjectId());
        modeleNull.setProjectId(10);
        assertEquals(10, modeleNull.getProjectId());
    }
    @Test
    public void testGetSubProjectId()
    {
        assertEquals(SUPROJECTID, modele.getSubProjectId());
        assertEquals(0, modeleNull.getSubProjectId());
    }
    
    @Test
    public void testSetSubProjectId()
    {
        modele.setSubProjectId(10);
        assertEquals(10, modele.getSubProjectId());
        modeleNull.setSubProjectId(10);
        assertEquals(10, modeleNull.getSubProjectId());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

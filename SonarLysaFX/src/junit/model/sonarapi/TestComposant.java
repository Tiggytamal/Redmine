package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
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
    public void getMapMetriques() throws IllegalAccessException
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
    public void getId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void setId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getId());
    }
    
    @Test
    public void getKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
    }
    
    @Test
    public void setKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getKey());
    }
      
    @Test
    public void getNom()
    {
        assertEquals(NOM, modele.getNom());
        assertNull(modeleNull.getNom());
    }
    
    @Test
    public void setNom()
    {
        modele.setNom(NEWVAL);
        assertEquals(NEWVAL, modele.getNom());
        modeleNull.setNom(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getNom());
    }
    
    @Test
    public void getDescritpion()
    {
        assertEquals(DESC, modele.getDescritpion());
        assertNull(modeleNull.getDescritpion());
    }
    
    @Test
    public void setDescritpion()
    {
        modele.setDescritpion(NEWVAL);
        assertEquals(NEWVAL, modele.getDescritpion());
        modeleNull.setDescritpion(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getDescritpion());
    }
    
    @Test
    public void getQualifier()
    {
        assertEquals(QUALIFIER, modele.getQualifier());
        assertNull(modeleNull.getQualifier());
    }
    
    @Test
    public void setQualifier()
    {
        modele.setQualifier(NEWVAL);
        assertEquals(NEWVAL, modele.getQualifier());
        modeleNull.setQualifier(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getQualifier());
    }
    
    @Test
    public void getLangage()
    {
        assertEquals(LANGUAGE, modele.getLangage());
        assertNull(modeleNull.getLangage());
    }
    
    @Test
    public void setLangage()
    {
        modele.setLangage(NEWVAL);
        assertEquals(NEWVAL, modele.getLangage());
        modeleNull.setLangage(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getLangage());
    }
    
    @Test
    public void getPath()
    {
        assertEquals(PATH, modele.getPath());
        assertNull(modeleNull.getPath());
    }
    
    @Test
    public void setPath()
    {
        modele.setPath(NEWVAL);
        assertEquals(NEWVAL, modele.getPath());
        modeleNull.setPath(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getPath());
    }
    
    @Test
    public void getMetriques()
    {
        assertEquals(METRIQUE, modele.getMetriques());
        assertNotNull(modeleNull.getMetriques());
    }
    
    @Test
    public void setMetriques()
    {
        List<Metrique> liste = new ArrayList<>();
        modele.setMetriques(liste);
        assertEquals(liste, modele.getMetriques());
        modeleNull.setMetriques(liste);
        assertEquals(liste, modeleNull.getMetriques());
    }
    
    @Test
    public void getUuid()
    {
        assertEquals(UUID, modele.getUuid());
        assertNull(modeleNull.getUuid());
    }
    
    @Test
    public void setUuid()
    {
        modele.setUuid(NEWVAL);
        assertEquals(NEWVAL, modele.getUuid());
        modeleNull.setUuid(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getUuid());
    }
        
    @Test
    public void isEnabled()
    {
        assertTrue(modele.isEnabled());
        assertFalse(modeleNull.isEnabled());
    }
    
    @Test
    public void setEnabled()
    {
        modele.setEnabled(false);
        assertFalse(modele.isEnabled());
        modeleNull.setEnabled(true);
        assertTrue(modeleNull.isEnabled());
    }
    
    @Test
    public void getLongName()
    {
        assertEquals(LONGNAME, modele.getLongName());
        assertNull(modeleNull.getLongName());
    }
    
    @Test
    public void setLongName()
    {
        modele.setLongName(NEWVAL);
        assertEquals(NEWVAL, modele.getLongName());
        modeleNull.setKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getKey());
    }
    
    @Test
    public void getProjectId()
    {
        assertEquals(PROJECTID, modele.getProjectId());
        assertEquals(0, modeleNull.getProjectId());
    }
    
    @Test
    public void setProjectId()
    {
        modele.setProjectId(10);
        assertEquals(10, modele.getProjectId());
        modeleNull.setProjectId(10);
        assertEquals(10, modeleNull.getProjectId());
    }
    @Test
    public void getSubProjectId()
    {
        assertEquals(SUPROJECTID, modele.getSubProjectId());
        assertEquals(0, modeleNull.getSubProjectId());
    }
    
    @Test
    public void setSubProjectId()
    {
        modele.setSubProjectId(10);
        assertEquals(10, modele.getSubProjectId());
        modeleNull.setSubProjectId(10);
        assertEquals(10, modeleNull.getSubProjectId());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Vue;

public class TestVue
{
    /*---------- ATTRIBUTS ----------*/

    private Vue modele;
    private Vue modeleNull;
    private static final String NAME = "name";
    private static final String KEY = "key";
    private static final String SELECTIONMODE = "nom";
    private static final String DESCRIPTION = "description";
    private static final List<String> LISTECLEFSCOMPOSANTS = new ArrayList<>();
    private static final boolean SELECTED = true;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Vue(KEY, NAME, SELECTED, SELECTIONMODE, DESCRIPTION, LISTECLEFSCOMPOSANTS);
        modeleNull = new Vue();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testToString()
    {
        modele = new Vue(KEY, NAME);
        assertTrue(modele.toString().contains(KEY));
        assertTrue(modele.toString().contains(NAME));
    }
    
    @Test
    public void getName()
    {
        assertEquals(NAME, modele.getName());
        assertNull(modeleNull.getName());
    }
    
    @Test
    public void setName()
    {
        modele.setName(NEWVAL);
        assertEquals(NEWVAL, modele.getName());
        modeleNull.setName(NAME);
        assertEquals(NAME, modeleNull.getName());
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
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
      
    @Test
    public void getSelectionMode()
    {
        assertEquals(SELECTIONMODE, modele.getSelectionMode());
        assertNull(modeleNull.getSelectionMode());
    }
    
    @Test
    public void setSelectionMode()
    {
        modele.setSelectionMode(NEWVAL);
        assertEquals(NEWVAL, modele.getSelectionMode());
        modeleNull.setSelectionMode(SELECTIONMODE);
        assertEquals(SELECTIONMODE, modeleNull.getSelectionMode());
    }
    
    @Test
    public void getDescription()
    {
        assertEquals(DESCRIPTION, modele.getDescription());
        assertNull(modeleNull.getDescription());
    }
    
    @Test
    public void setDescription()
    {
        modele.setDescription(NEWVAL);
        assertEquals(NEWVAL, modele.getDescription());
        modeleNull.setDescription(DESCRIPTION);
        assertEquals(DESCRIPTION, modeleNull.getDescription());
    }
    
    @Test
    public void getListeClefsComposants()
    {
        assertEquals(LISTECLEFSCOMPOSANTS, modele.getListeClefsComposants());
        assertNull(modeleNull.getListeClefsComposants());
    }
    
    @Test
    public void setListeClefsComposants()
    {
        modele.setListeClefsComposants(new ArrayList<>());
        assertTrue(modele.getListeClefsComposants().isEmpty());
        modeleNull.setListeClefsComposants(LISTECLEFSCOMPOSANTS);
        assertEquals(LISTECLEFSCOMPOSANTS, modeleNull.getListeClefsComposants());
    }
        
    @Test
    public void isSelected()
    {
        assertTrue(modele.isSelected());
        assertFalse(modeleNull.isSelected());
    }
    
    @Test
    public void setSelected()
    {
        modele.setSelected(false);
        assertFalse(modele.isSelected());
        modeleNull.setSelected(SELECTED);
        assertTrue(modeleNull.isSelected());
    }
        
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

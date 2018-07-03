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
    public void testControleVue()
    {
        Vue vue = null;
        assertFalse(Vue.controleVue(vue));
        vue = new Vue(null, null);
        assertFalse(Vue.controleVue(vue));
        vue.setKey("");
        assertFalse(Vue.controleVue(vue));
        vue.setKey("key");
        assertFalse(Vue.controleVue(vue));
        vue.setName("");
        assertFalse(Vue.controleVue(vue));
        vue.setName("Name");
        assertTrue(Vue.controleVue(vue));
    }
    
    @Test
    public void testToString()
    {
        modele = new Vue(KEY, NAME);
        assertTrue(modele.toString().contains(KEY));
        assertTrue(modele.toString().contains(NAME));
    }
    
    @Test
    public void testGetName()
    {
        assertEquals(NAME, modele.getName());
        assertNull(modeleNull.getName());
    }
    
    @Test
    public void testSetName()
    {
        modele.setName(NEWVAL);
        assertEquals(NEWVAL, modele.getName());
        modeleNull.setName(NAME);
        assertEquals(NAME, modeleNull.getName());
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
    public void testGetSelectionMode()
    {
        assertEquals(SELECTIONMODE, modele.getSelectionMode());
        assertNull(modeleNull.getSelectionMode());
    }
    
    @Test
    public void testSetSelectionMode()
    {
        modele.setSelectionMode(NEWVAL);
        assertEquals(NEWVAL, modele.getSelectionMode());
        modeleNull.setSelectionMode(SELECTIONMODE);
        assertEquals(SELECTIONMODE, modeleNull.getSelectionMode());
    }
    
    @Test
    public void testGetDescription()
    {
        assertEquals(DESCRIPTION, modele.getDescription());
        assertNull(modeleNull.getDescription());
    }
    
    @Test
    public void testSetDescription()
    {
        modele.setDescription(NEWVAL);
        assertEquals(NEWVAL, modele.getDescription());
        modeleNull.setDescription(DESCRIPTION);
        assertEquals(DESCRIPTION, modeleNull.getDescription());
    }
    
    @Test
    public void testGetListeClefsComposants()
    {
        assertEquals(LISTECLEFSCOMPOSANTS, modele.getListeClefsComposants());
        assertNull(modeleNull.getListeClefsComposants());
    }
    
    @Test
    public void testSetListeClefsComposants()
    {
        modele.setListeClefsComposants(new ArrayList<>());
        assertTrue(modele.getListeClefsComposants().isEmpty());
        modeleNull.setListeClefsComposants(LISTECLEFSCOMPOSANTS);
        assertEquals(LISTECLEFSCOMPOSANTS, modeleNull.getListeClefsComposants());
    }
        
    @Test
    public void testIsSelected()
    {
        assertTrue(modele.isSelected());
        assertFalse(modeleNull.isSelected());
    }
    
    @Test
    public void testSetSelected()
    {
        modele.setSelected(false);
        assertFalse(modele.isSelected());
        modeleNull.setSelected(SELECTED);
        assertTrue(modeleNull.isSelected());
    }
        
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

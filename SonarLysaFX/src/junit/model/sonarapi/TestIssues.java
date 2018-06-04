package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Composant;
import model.sonarapi.Issue;
import model.sonarapi.Issues;
import model.sonarapi.Paging;

public class TestIssues
{
    /*---------- ATTRIBUTS ----------*/

    private Issues modele;
    private Issues modeleNull;
    private static final int TOTAL = 10;
    private static final int P = 20;
    private static final int PS = 30;
    private static final Paging PAGING = new Paging();
    private static final List<Composant> COMPOSANTS = new ArrayList<>();
    private static final List<Issue> ISSUES = new ArrayList<>();
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        COMPOSANTS.add(new Composant());
        ISSUES.add(new Issue());
        modele = new Issues(TOTAL, P, PS, PAGING, COMPOSANTS, ISSUES);
        modeleNull = new Issues();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void getTotal()
    {
        assertEquals(TOTAL, modele.getTotal());
        assertEquals(0, modeleNull.getTotal());
    }
    
    @Test
    public void getP()
    {
        assertEquals(P, modele.getP());
        assertEquals(0, modeleNull.getP());
    }
    
    @Test
    public void getPs()
    {
        assertEquals(PS, modele.getPs());
        assertEquals(0, modeleNull.getPs());
    }
    
    @Test
    public void getPaging()
    {
        assertEquals(PAGING, modele.getPaging());
        assertNull(modeleNull.getPaging());
    }
    
    @Test
    public void getComposants()
    {
        assertEquals(COMPOSANTS, modele.getComposants());
        assertNotNull(modeleNull.getComposants());
        assertTrue(modeleNull.getComposants().isEmpty());
    }
    
    @Test
    public void getListIssues()
    {
        assertEquals(ISSUES, modele.getListIssues());
        assertNotNull(modeleNull.getListIssues());
        assertTrue(modeleNull.getListIssues().isEmpty());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

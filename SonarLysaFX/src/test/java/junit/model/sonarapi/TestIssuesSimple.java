package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Composant;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.IssuesSimple;
import model.rest.sonarapi.Paging;

public class TestIssuesSimple
{
    /*---------- ATTRIBUTS ----------*/

    private IssuesSimple modele;
    private IssuesSimple modeleNull;
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
        modele = new IssuesSimple(TOTAL, P, PS, PAGING, COMPOSANTS, ISSUES);
        modeleNull = new IssuesSimple();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetTotal()
    {
        assertEquals(TOTAL, modele.getTotal());
        assertEquals(0, modeleNull.getTotal());
    }
    
    @Test
    public void testGetP()
    {
        assertEquals(P, modele.getP());
        assertEquals(0, modeleNull.getP());
    }
    
    @Test
    public void testGetPs()
    {
        assertEquals(PS, modele.getPs());
        assertEquals(0, modeleNull.getPs());
    }
    
    @Test
    public void testGetPaging()
    {
        assertEquals(PAGING, modele.getPaging());
        assertNotNull(modeleNull.getPaging());
    }
    
    @Test
    public void testGetComposants()
    {
        assertEquals(COMPOSANTS, modele.getComposants());
        assertNotNull(modeleNull.getComposants());
        assertEquals(0, modeleNull.getComposants().size());
    }
    
    @Test
    public void testGetListIssues()
    {
        assertEquals(ISSUES, modele.getListIssues());
        assertNotNull(modeleNull.getListIssues());
        assertEquals(0, modeleNull.getListIssues().size());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

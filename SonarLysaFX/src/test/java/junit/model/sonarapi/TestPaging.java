package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Paging;

public class TestPaging
{
    /*---------- ATTRIBUTS ----------*/

    private Paging modele;
    private Paging modeleNull;
    private static final int PAGEINDEX = 10;
    private static final int TOTAL = 20;
    private static final int PAGESIZE = 30;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Paging(PAGEINDEX, PAGESIZE, TOTAL);
        modeleNull = new Paging();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetTotal()
    {
        assertEquals(TOTAL, modele.getTotal());
        assertEquals(0, modeleNull.getTotal());
    }
    
    @Test
    public void testSetTotal()
    {
        modele.setTotal(50);
        assertEquals(50, modele.getTotal());
        modeleNull.setTotal(12);
        assertEquals(12, modeleNull.getTotal());
    }
    
    @Test
    public void testGetPageIndex()
    {
        assertEquals(PAGEINDEX, modele.getPageIndex());
        assertEquals(0, modeleNull.getPageIndex());
    }
    
    @Test
    public void testSetPageIndex()
    {
        modele.setPageIndex(50);
        assertEquals(50, modele.getPageIndex());
        modeleNull.setPageIndex(PAGEINDEX);
        assertEquals(PAGEINDEX, modeleNull.getPageIndex());
    }
    
    @Test
    public void testGetPageSize()
    {
        assertEquals(PAGESIZE, modele.getPageSize());
        assertEquals(0, modeleNull.getPageSize());
    }
    
    @Test
    public void testSetPageSize()
    {
        modele.setPageSize(50);
        assertEquals(50, modele.getPageSize());
        modeleNull.setPageSize(12);
        assertEquals(12, modeleNull.getPageSize());
    }
}
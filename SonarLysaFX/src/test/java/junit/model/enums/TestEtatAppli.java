package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.EtatAppli;

public class TestEtatAppli implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(6, EtatAppli.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(EtatAppli.PREC, EtatAppli.valueOf(EtatAppli.PREC.toString()));
    }
}

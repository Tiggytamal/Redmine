package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeCom;

public class TestTypeCom implements TestEnums
{

    @Override
    @Test
    public void testConstructeur()
    {
        assertEquals(TypeCom.RELANCE, TypeCom.valueOf(TypeCom.RELANCE.toString()));
    }

    @Override
    @Test
    public void testSize()
    {
        assertEquals(2, TypeCom.values().length);
    }

}

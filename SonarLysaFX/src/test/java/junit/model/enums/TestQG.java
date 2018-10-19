package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.QG;
import utilities.Statics;

public class TestQG implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(QG.ERROR, QG.valueOf(QG.ERROR.toString()));  
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, QG.values().length);
    }
    
    @Test
    public void testFrom()
    {
        assertEquals(QG.OK, QG.from("OK"));
        assertEquals("OK", QG.OK.getValeur());
        assertEquals(QG.WARN, QG.from("WARN"));
        assertEquals("WARN", QG.WARN.getValeur());
        assertEquals(QG.ERROR, QG.from("ERROR"));
        assertEquals("ERROR", QG.ERROR.getValeur());
        assertEquals(QG.NONE, QG.from("NONE"));
        assertEquals("NONE", QG.NONE.getValeur());

        // Test lancement exception
        List<String> listeException = new ArrayList<>();
        listeException.add("\0OK");
        listeException.add("\0WARN");
        listeException.add("\0ERROR");
        listeException.add("\0NONE");
        
        for (String string : listeException)
        {
            try
            {
                QG.from(string);
            }
            catch (IllegalArgumentException e)
            {
                assertEquals("model.enums.sonarapi.Status inconnu : " + string, e.getMessage());
                continue;
            }
            fail("Pas d'envoi d'esception avec " + string);
        }
        
        listeException.clear();
        listeException.add(null);
        listeException.add(Statics.EMPTY);
        
        for (String string : listeException)
        {
            try
            {
                QG.from(string);
            }
            catch (IllegalArgumentException e)
            {
                assertEquals("model.enums.sonarapi.Status - argument vide ou nul.", e.getMessage());
                continue;
            }
            fail("Pas d'envoi d'esception avec " + string);
        }
    }
    
    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(QG.class, "Valeur");
        Constructor<Object> constructor = Whitebox.getConstructor(inner);
        constructor.setAccessible(true);
        try
        {
            constructor.newInstance();
        } catch (InvocationTargetException e)
        {
            assertEquals(AssertionError.class, e.getCause().getClass());
        }
    }

}

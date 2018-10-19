package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.EtatAnoRTC;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de test de l'énumération EtatNaoRTC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestEtatNaoRTC implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(9, EtatAnoRTC.values().length);
    }
    
    @Test
    public void testFrom()
    {
        assertEquals(EtatAnoRTC.NOUVELLE, EtatAnoRTC.from("Nouvelle"));
        assertEquals("Nouvelle", EtatAnoRTC.NOUVELLE.getValeur());
        assertEquals(EtatAnoRTC.OUVERTE, EtatAnoRTC.from("Ouverte"));
        assertEquals("Ouverte", EtatAnoRTC.OUVERTE.getValeur());
        assertEquals(EtatAnoRTC.ENCOURS, EtatAnoRTC.from("En cours"));
        assertEquals("En cours", EtatAnoRTC.ENCOURS.getValeur());
        assertEquals(EtatAnoRTC.RESOLUE, EtatAnoRTC.from("Résolue"));
        assertEquals("Résolue", EtatAnoRTC.RESOLUE.getValeur());
        assertEquals(EtatAnoRTC.VMOE, EtatAnoRTC.from("En attente vérification MOE"));
        assertEquals("En attente vérification MOE", EtatAnoRTC.VMOE.getValeur());
        assertEquals(EtatAnoRTC.VMOA, EtatAnoRTC.from("En attente validation/homologation MOA"));
        assertEquals("En attente validation/homologation MOA", EtatAnoRTC.VMOA.getValeur());
        assertEquals(EtatAnoRTC.VERIFIEE, EtatAnoRTC.from("Vérifiée"));
        assertEquals("Vérifiée", EtatAnoRTC.VERIFIEE.getValeur());
        assertEquals(EtatAnoRTC.CLOSE, EtatAnoRTC.from("Close"));
        assertEquals("Close", EtatAnoRTC.CLOSE.getValeur());
        assertEquals(EtatAnoRTC.REOUVERTE, EtatAnoRTC.from("Réouverte"));
        assertEquals("Réouverte", EtatAnoRTC.REOUVERTE.getValeur());

        // Test lancement exception
        List<String> listeException = new ArrayList<>();
        listeException.add("\0Nouvelle");
        listeException.add("\0Ouverte");
        listeException.add("\0En cours");
        listeException.add("\0Résolue");
        listeException.add("\0En attente vérification MOE");
        listeException.add("\0En attente validation/homologation MOA");
        listeException.add("\0Vérifiée");
        listeException.add("\0Close");
        listeException.add("\0Réouverte");
        
        for (String string : listeException)
        {
            try
            {
                EtatAnoRTC.from(string);
            }
            catch (TechnicalException e)
            {
                assertEquals("model.enums.EtatAnoRTC.from - etat envoyé inconnu :" + string, e.getMessage());
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
                EtatAnoRTC.from(string);
            }
            catch (TechnicalException e)
            {
                assertEquals("model.enums.EtatAnoRTC.from - etat envoyé nul ou vide.", e.getMessage());
                continue;
            }
            fail("Pas d'envoi d'esception avec " + string);
        }
    }
    
    @Test
    public void testGetAction()
    {
        assertEquals("Accepter", EtatAnoRTC.NOUVELLE.getAction());
        assertEquals("Commencer à travailler", EtatAnoRTC.OUVERTE.getAction());
        assertEquals("Terminer le travail", EtatAnoRTC.ENCOURS.getAction());
        assertEquals("Clore", EtatAnoRTC.RESOLUE.getAction());
        assertEquals("Vérifier OK", EtatAnoRTC.VMOE.getAction());
        assertEquals("Clore", EtatAnoRTC.VERIFIEE.getAction());
        assertEquals("Valider et clore", EtatAnoRTC.VMOA.getAction());
        assertEquals("", EtatAnoRTC.CLOSE.getAction());
        assertEquals("Commencer à travailler", EtatAnoRTC.REOUVERTE.getAction());
    }
    
    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(EtatAnoRTC.class, "Valeur");
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
    
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(EtatAnoRTC.NOUVELLE, EtatAnoRTC.valueOf(EtatAnoRTC.NOUVELLE.toString()));     
    }
}

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
 * Classe de test de l'�num�ration EtatNaoRTC
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class TestEtatAnoRTC implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(14, EtatAnoRTC.values().length);
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
        assertEquals(EtatAnoRTC.RESOLUE, EtatAnoRTC.from("R�solue"));
        assertEquals("R�solue", EtatAnoRTC.RESOLUE.getValeur());
        assertEquals(EtatAnoRTC.VMOE, EtatAnoRTC.from("En attente v�rification MOE"));
        assertEquals("En attente v�rification MOE", EtatAnoRTC.VMOE.getValeur());
        assertEquals(EtatAnoRTC.VMOA, EtatAnoRTC.from("En attente validation/homologation MOA"));
        assertEquals("En attente validation/homologation MOA", EtatAnoRTC.VMOA.getValeur());
        assertEquals(EtatAnoRTC.VERIFIEE, EtatAnoRTC.from("V�rifi�e"));
        assertEquals("V�rifi�e", EtatAnoRTC.VERIFIEE.getValeur());
        assertEquals(EtatAnoRTC.CLOSE, EtatAnoRTC.from("Close"));
        assertEquals("Close", EtatAnoRTC.CLOSE.getValeur());
        assertEquals(EtatAnoRTC.REOUVERTE, EtatAnoRTC.from("R�ouverte"));
        assertEquals("R�ouverte", EtatAnoRTC.REOUVERTE.getValeur());
        assertEquals(EtatAnoRTC.REJETEE, EtatAnoRTC.from("Rejet�e"));
        assertEquals("Rejet�e", EtatAnoRTC.REJETEE.getValeur());
        assertEquals(EtatAnoRTC.EDITEUR, EtatAnoRTC.from("Correctif �diteur test�"));
        assertEquals("Correctif �diteur test�", EtatAnoRTC.EDITEUR.getValeur());
        assertEquals(EtatAnoRTC.ATTEDITEUR, EtatAnoRTC.from("En attente correctif �diteur"));
        assertEquals("En attente correctif �diteur", EtatAnoRTC.ATTEDITEUR.getValeur());
        assertEquals(EtatAnoRTC.DENOUEMENT, EtatAnoRTC.from("En attente de d�nouement"));
        assertEquals("En attente de d�nouement", EtatAnoRTC.DENOUEMENT.getValeur());
        assertEquals(EtatAnoRTC.ABANDONNEE, EtatAnoRTC.from("Abandonn�e"));
        assertEquals("Abandonn�e", EtatAnoRTC.ABANDONNEE.getValeur());

        // Test lancement exception
        List<String> listeException = new ArrayList<>();
        listeException.add("\0Nouvelle");
        listeException.add("\0Ouverte");
        listeException.add("\0En cours");
        listeException.add("\0R�solue");
        listeException.add("\0En attente v�rification MOE");
        listeException.add("\0En attente validation/homologation MOA");
        listeException.add("\0V�rifi�e");
        listeException.add("\0Close");
        listeException.add("\0R�ouverte");
        listeException.add("\0Rejet�e");
        listeException.add("\0En attente de d�nouement");
        listeException.add("\0Abandonn�e");
        listeException.add("\0En attente correctif �diteur");
        listeException.add("\0Correctif �diteur test�");

        for (String string : listeException)
        {
            try
            {
                EtatAnoRTC.from(string);
            }
            catch (TechnicalException e)
            {
                assertEquals("model.enums.EtatAnoRTC.from - etat envoy� inconnu :" + string, e.getMessage());
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
                assertEquals("model.enums.EtatAnoRTC.from - etat envoy� nul ou vide.", e.getMessage());
                continue;
            }
            fail("Pas d'envoi d'esception avec " + string);
        }
    }

    @Test
    public void testGetAction()
    {
        assertEquals("Accepter", EtatAnoRTC.NOUVELLE.getAction());
        assertEquals("Commencer � travailler", EtatAnoRTC.OUVERTE.getAction());
        assertEquals("Terminer le travail", EtatAnoRTC.ENCOURS.getAction());
        assertEquals("Clore", EtatAnoRTC.RESOLUE.getAction());
        assertEquals("V�rifier OK", EtatAnoRTC.VMOE.getAction());
        assertEquals("Clore", EtatAnoRTC.VERIFIEE.getAction());
        assertEquals("Valider et clore", EtatAnoRTC.VMOA.getAction());
        assertEquals("", EtatAnoRTC.CLOSE.getAction());
        assertEquals("Commencer � travailler", EtatAnoRTC.REOUVERTE.getAction());
        assertEquals("", EtatAnoRTC.REJETEE.getAction());
        assertEquals("Terminer le travail", EtatAnoRTC.EDITEUR.getAction());
        assertEquals("Tester correctif", EtatAnoRTC.ATTEDITEUR.getAction());
        assertEquals("Reprendre", EtatAnoRTC.DENOUEMENT.getAction());
        assertEquals("", EtatAnoRTC.CLOSE.getAction());
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
        }
        catch (InvocationTargetException e)
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

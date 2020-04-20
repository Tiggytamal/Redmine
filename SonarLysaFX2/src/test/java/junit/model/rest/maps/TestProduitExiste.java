package junit.model.rest.maps;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.maps.ProduitExiste;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProduitExiste extends TestAbstractModele<ProduitExiste>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetStatut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatut(), s -> objetTest.setStatut(s));
    }

    @Test
    public void testGetCode(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCode(), s -> objetTest.setCode(s));
    }

    @Test
    public void testGetLibelle(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.existe(), b -> objetTest.setExiste(b));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

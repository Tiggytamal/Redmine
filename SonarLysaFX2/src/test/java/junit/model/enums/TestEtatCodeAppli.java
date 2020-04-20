package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.enums.EtatCodeAppli;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEtatCodeAppli extends TestAbstractEnum<EtatCodeAppli>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatCodeAppli.valueOf(EtatCodeAppli.ERREUR.toString())).isEqualTo(EtatCodeAppli.ERREUR);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatCodeAppli.values().length).isEqualTo(3);
    }

    @Test
    public void testTostring(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatCodeAppli.ERREUR.toString()).isEqualTo("ERREUR");
        assertThat(EtatCodeAppli.NA.toString()).isEqualTo("N/A");
        assertThat(EtatCodeAppli.OK.toString()).isEqualTo("OK");
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EtatCodeAppli.ERREUR.getValeur()).isEqualTo("ERREUR");
        assertThat(EtatCodeAppli.NA.getValeur()).isEqualTo("N/A");
        assertThat(EtatCodeAppli.OK.getValeur()).isEqualTo("OK");
    }
    
    @Test
    public void testCalculCodeAppli_NA()
    {
        ComposantBase compo = ComposantBase.build(KEY, NOM);
        compo.setControleAppli(false);
        
        // Test retour NA
        assertThat(EtatCodeAppli.calculCodeAppli(compo)).isEqualTo(EtatCodeAppli.NA);
        
        // Test retour non NA
        compo.setControleAppli(true);
        assertThat(EtatCodeAppli.calculCodeAppli(compo)).isNotEqualTo(EtatCodeAppli.NA);
    }
    
    @Test
    public void testCalculCodeAppli_OK()
    {
        ComposantBase compo = ComposantBase.build(KEY, NOM);
        compo.setControleAppli(true);
        compo.setAppli(Application.getApplication("ABCD", true));
        
        // test retour OK
        assertThat(EtatCodeAppli.calculCodeAppli(compo)).isEqualTo(EtatCodeAppli.OK);
        
        // Test retour non OK
        compo.setAppli(Application.getApplication("ABCD", false));
        assertThat(EtatCodeAppli.calculCodeAppli(compo)).isNotEqualTo(EtatCodeAppli.OK);
    }
    
    @Test
    public void testCalculCodeAppli_ERREUR()
    {
        ComposantBase compo = ComposantBase.build(KEY, NOM);
        compo.setControleAppli(true);
        compo.setAppli(Application.getApplication("ABCD", false));
        
        // test retour ERREUR
        assertThat(EtatCodeAppli.calculCodeAppli(compo)).isEqualTo(EtatCodeAppli.ERREUR);
        
        // Test retour non ERREUR
        compo.setAppli(Application.getApplication("ABCD", true));
        assertThat(EtatCodeAppli.calculCodeAppli(compo)).isNotEqualTo(EtatCodeAppli.ERREUR);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

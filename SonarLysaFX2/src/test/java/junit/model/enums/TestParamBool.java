package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ParamBool;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParamBool extends TestAbstractEnum<ParamBool>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        assertThat(ParamBool.valueOf(ParamBool.FICHIERPICAUTO.toString())).isEqualTo(ParamBool.FICHIERPICAUTO);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        assertThat(ParamBool.values().length).isEqualTo(1);
    }

    @Test
    public void testGetNom()
    {
        assertThat(ParamBool.FICHIERPICAUTO.getNom()).isNotEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

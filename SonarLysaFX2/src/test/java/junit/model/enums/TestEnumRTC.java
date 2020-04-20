package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.EnumRTC;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEnumRTC extends TestAbstractEnum<EnumRTC>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EnumRTC.values().length).isEqualTo(15);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EnumRTC.ENTITERESPCORRECTION.getValeur()).isEqualTo("fr.ca.cat.attribut.entiteresponsablecorrection");
        assertThat(EnumRTC.ENVIRONNEMENT.getValeur()).isEqualTo("fr.ca.cat.attribut.environnement");
        assertThat(EnumRTC.NATURE.getValeur()).isEqualTo("NatureProbleme");
        assertThat(EnumRTC.EDITION.getValeur()).isEqualTo("fr.ca.cat.attribut.edition");
        assertThat(EnumRTC.EDITIONSICIBLE.getValeur()).isEqualTo("fr.ca.cat.attribut.editionsicible");
        assertThat(EnumRTC.EDITIONSI.getValeur()).isEqualTo("editionSI");
        assertThat(EnumRTC.IMPORTANCE.getValeur()).isEqualTo("NiveauImportance");
        assertThat(EnumRTC.CRITICITE.getValeur()).isEqualTo("fr.ca.cat.attribut.criticite");
        assertThat(EnumRTC.ORIGINE.getValeur()).isEqualTo("Origine");
        assertThat(EnumRTC.ORIGINE2.getValeur()).isEqualTo("fr.ca.cat.attribut.origine");
        assertThat(EnumRTC.CLARITY.getValeur()).isEqualTo("fr.ca.cat.attribut.codeprojetclarity");
        assertThat(EnumRTC.CODECLARITY.getValeur()).isEqualTo("codeprojet");
        assertThat(EnumRTC.DATELIVHOMO.getValeur()).isEqualTo("fr.ca.cat.attribut.datedelivraison");
        assertThat(EnumRTC.TROUVEDANS.getValeur()).isEqualTo("fr.ca.cat.attribut.trouvedans");
        assertThat(EnumRTC.COMPTERENDU.getValeur()).isEqualTo("fr.ca.cat.attribut.comptesrendus");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(EnumRTC.valueOf(EnumRTC.IMPORTANCE.toString())).isEqualTo(EnumRTC.IMPORTANCE);
    }
}

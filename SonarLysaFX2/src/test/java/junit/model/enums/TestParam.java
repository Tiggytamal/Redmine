package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.Param;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParam extends TestAbstractEnum<Param>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Param.valueOf(Param.FILTREDATASTAGE.toString())).isEqualTo(Param.FILTREDATASTAGE);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Param.values().length).isEqualTo(20);
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Param.FILTREDATASTAGE.getNom()).isNotEmpty();
        assertThat(Param.FILTRECOBOL.getNom()).isNotEmpty();
        assertThat(Param.FILTREANDROID.getNom()).isNotEmpty();
        assertThat(Param.FILTREIOS.getNom()).isNotEmpty();
        assertThat(Param.FICHIERVERSION.getNom()).isNotEmpty();
        assertThat(Param.REPSONAR.getNom()).isNotEmpty();
        assertThat(Param.LIENSCOMPOS.getNom()).isNotEmpty();
        assertThat(Param.LIENSRTC.getNom()).isNotEmpty();
        assertThat(Param.NOMQGDATASTAGE.getNom()).isNotEmpty();
        assertThat(Param.URLSONAR.getNom()).isNotEmpty();
        assertThat(Param.URLSONARMC.getNom()).isNotEmpty();
        assertThat(Param.URLREPACK.getNom()).isNotEmpty();
        assertThat(Param.URLRTC.getNom()).isNotEmpty();
        assertThat(Param.URLAPIAPPCATS.getNom()).isNotEmpty();
        assertThat(Param.URLMAPS.getNom()).isNotEmpty();
        assertThat(Param.URLMAPSPRODUIT.getNom()).isNotEmpty();
        assertThat(Param.RTCLOTCHC.getNom()).isNotEmpty();
        assertThat(Param.URLAPPCATSAPPLI.getNom()).isNotEmpty();
    }

    @Test
    public void testIsPerso(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Param.FILTREDATASTAGE.isPerso()).isFalse();
        assertThat(Param.FILTRECOBOL.isPerso()).isFalse();
        assertThat(Param.FILTREANDROID.isPerso()).isFalse();
        assertThat(Param.FILTREIOS.isPerso()).isFalse();
        assertThat(Param.FILTREANGULAR.isPerso()).isFalse();
        assertThat(Param.ABSOLUTEPATHRAPPORT.isPerso()).isFalse();
        assertThat(Param.FICHIERVERSION.isPerso()).isFalse();
        assertThat(Param.REPSONAR.isPerso()).isFalse();
        assertThat(Param.LIENSCOMPOS.isPerso()).isFalse();
        assertThat(Param.LIENSRTC.isPerso()).isFalse();
        assertThat(Param.NOMQGDATASTAGE.isPerso()).isFalse();
        assertThat(Param.URLSONAR.isPerso()).isFalse();
        assertThat(Param.URLSONARMC.isPerso()).isFalse();
        assertThat(Param.URLREPACK.isPerso()).isFalse();
        assertThat(Param.URLRTC.isPerso()).isFalse();
        assertThat(Param.URLAPIAPPCATS.isPerso()).isFalse();
        assertThat(Param.URLAPPCATSAPPLI.isPerso()).isFalse();
        assertThat(Param.URLMAPS.isPerso()).isFalse();
        assertThat(Param.URLMAPSPRODUIT.isPerso()).isFalse();
        assertThat(Param.RTCLOTCHC.isPerso()).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

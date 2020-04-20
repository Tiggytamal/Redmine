package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ParamSpec;
import model.enums.TypeParamSpec;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestParamSpec extends TestAbstractEnum<ParamSpec>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ParamSpec.valueOf(ParamSpec.TEXTEANO.toString())).isEqualTo(ParamSpec.TEXTEANO);
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ParamSpec.values().length).isEqualTo(20);
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ParamSpec.TEXTEPANORECAP.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANORELANCE.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANOAPPLI.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANO.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANOOBSOLETE.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANOCLOSE.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANOABANDONNEE.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANOSECURITE.getNom()).isNotEmpty();
        assertThat(ParamSpec.TITREASSIGNATIONANO.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEASSIGNATIONANO.getNom()).isNotEmpty();
        assertThat(ParamSpec.TITRECREERANORTC.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTECREERANORTC.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTECREERANORTCAPPLI.getNom()).isNotEmpty();        
        assertThat(ParamSpec.TITRERELANCEANORTC.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTERELANCEANORTC.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTERELANCEANORTCAPPLI.getNom()).isNotEmpty();
        assertThat(ParamSpec.SIGNATURE.getNom()).isNotEmpty();
        assertThat(ParamSpec.MEMBRESAQP.getNom()).isNotEmpty();
        assertThat(ParamSpec.TEXTEANOAPPLIFAUX.getNom()).isNotEmpty();
    }

    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ParamSpec.TEXTEPANORECAP.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANORELANCE.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANOAPPLI.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANOOBSOLETE.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANOCLOSE.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANOABANDONNEE.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANO.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEANOSECURITE.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TITREASSIGNATIONANO.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTEASSIGNATIONANO.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TITRECREERANORTC.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTECREERANORTC.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTECREERANORTCAPPLI.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TITRERELANCEANORTC.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTERELANCEANORTC.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.TEXTERELANCEANORTCAPPLI.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.SIGNATURE.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
        assertThat(ParamSpec.MEMBRESAQP.getType()).isEqualTo(TypeParamSpec.LISTVIEWNOM);
        assertThat(ParamSpec.TEXTEANOAPPLIFAUX.getType()).isEqualTo(TypeParamSpec.TEXTAREA);
    }

    @Test
    public void testIsPerso(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ParamSpec.TEXTEPANORECAP.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTEANORELANCE.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTEANOAPPLI.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTEANOOBSOLETE.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTEANO.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTEANOSECURITE.isPerso()).isFalse();
        assertThat(ParamSpec.TITREASSIGNATIONANO.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTEASSIGNATIONANO.isPerso()).isFalse();
        assertThat(ParamSpec.TITRECREERANORTC.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTECREERANORTC.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTECREERANORTCAPPLI.isPerso()).isFalse();
        assertThat(ParamSpec.TITRERELANCEANORTC.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTERELANCEANORTC.isPerso()).isFalse();
        assertThat(ParamSpec.TEXTERELANCEANORTCAPPLI.isPerso()).isFalse();
        assertThat(ParamSpec.MEMBRESAQP.isPerso()).isFalse();
        assertThat(ParamSpec.SIGNATURE.isPerso()).isTrue();
        assertThat(ParamSpec.TEXTEANOAPPLIFAUX.isPerso()).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

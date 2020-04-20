package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;
import static utilities.Statics.DATEINCO2099;
import static utilities.Statics.DATEINCONNUE;
import static utilities.Statics.EDINCONNUE;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.Edition;
import model.enums.TypeEdition;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEdition extends TestAbstractBDDModele<Edition, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = Edition.build(NOM, "numero");
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        assertThat(objetTest.getNumero()).isEqualTo("numero");
    }

    @Test
    public void testGetEditionInconnue(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test normale
        objetTest = Edition.getEditionInconnue("edition");
        assertThat(objetTest.getTypeEdition()).isEqualTo(TypeEdition.INCONNUE);
        assertThat(objetTest.getDateMEP()).isEqualTo(Statics.DATEINCO2099);
        assertThat(objetTest.getCommentaire()).isEqualTo("Inconnue dans la codification des Editions");
        assertThat(objetTest.getNumero()).isEqualTo(EDINCONNUE);
        assertThat(objetTest.getNom()).isEqualTo("edition");

        // Test editoin nulle ou vide
        objetTest = Edition.getEditionInconnue(null);
        assertThat(objetTest.getTypeEdition()).isEqualTo(TypeEdition.INCONNUE);
        assertThat(objetTest.getDateMEP()).isEqualTo(Statics.DATEINCO2099);
        assertThat(objetTest.getCommentaire()).isEqualTo("Inconnue dans la codification des Editions");
        assertThat(objetTest.getNumero()).isEqualTo(EDINCONNUE);
        assertThat(objetTest.getNom()).isEqualTo(EDINCONNUE);

        objetTest = Edition.getEditionInconnue(Statics.EMPTY);
        assertThat(objetTest.getTypeEdition()).isEqualTo(TypeEdition.INCONNUE);
        assertThat(objetTest.getDateMEP()).isEqualTo(Statics.DATEINCO2099);
        assertThat(objetTest.getCommentaire()).isEqualTo("Inconnue dans la codification des Editions");
        assertThat(objetTest.getNumero()).isEqualTo(EDINCONNUE);
        assertThat(objetTest.getNom()).isEqualTo(EDINCONNUE);

    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        objetTest.setNom(NOM);
        objetTest.setNumero(LOT123456);
        objetTest.setCommentaire(TESTSTRING);
        objetTest.setDateMEP(today);
        objetTest.setEditionMajeure(TESTSTRING);
        objetTest.setTypeEdition(TypeEdition.CDM);

        // Test simple
        Edition edition = ModelFactory.build(Edition.class);
        testSimpleEquals(edition);

        // Test autres paramètres
        edition.setCommentaire(TESTSTRING);
        assertThat(objetTest.equals(edition)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(edition.hashCode());
        
        edition.setDateMEP(today);
        assertThat(objetTest.equals(edition)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(edition.hashCode());
        
        edition.setEditionMajeure(TESTSTRING);
        assertThat(objetTest.equals(edition)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(edition.hashCode());
        
        edition.setNom(NOM);
        assertThat(objetTest.equals(edition)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(edition.hashCode());
        
        edition.setNumero(LOT123456);
        assertThat(objetTest.equals(edition)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(edition.hashCode());
        
        edition.setTypeEdition(TypeEdition.CDM);
        assertThat(objetTest.equals(edition)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(edition.hashCode());
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.setNumero(NOM);
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getNumero());
    }

    @Test
    public void testUpdate(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objet
        objetTest.setNumero("00.01.02.03");
        objetTest.setNom(NOM);
        Edition update = Edition.getEditionInconnue("test");

        // Test mise à jour
        objetTest.update(update);
        assertThat(NOM).isEqualTo(objetTest.getNom());
        assertThat(update.getCommentaire()).isEqualTo(objetTest.getCommentaire());
        assertThat(update.getTypeEdition()).isEqualTo(objetTest.getTypeEdition());
        assertThat(update.getDateMEP()).isEqualTo(objetTest.getDateMEP());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetNumero(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getNumero()).isEmpty();

        // Test setter et getter avec contrôle sur le set

        // string non correcte
        String string = "etat";
        objetTest.setNumero(string);
        assertThat(objetTest.getNumero()).isEmpty();

        // string null
        string = null;
        objetTest.setNumero(string);
        assertThat(objetTest.getNumero()).isEmpty();

        // string empty
        string = EMPTY;
        objetTest.setNumero(string);
        assertThat(objetTest.getNumero()).isEmpty();

        // string correct
        string = "00.02.04.06";
        objetTest.setNumero(string);
        assertThat(objetTest.getNumero()).isEqualTo(string);
    }

    @Test
    public void testGetCommentaire(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getCommentaire(), s -> objetTest.setCommentaire(s));
    }

    @Test
    public void testGetDateMEP(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test sans initialisation
        assertThat(objetTest.getDateMEP()).isEqualTo(DATEINCO2099);

        // Test setter et getter
        objetTest.setDateMEP(DATEINCONNUE);
        assertThat(objetTest.getDateMEP()).isEqualTo(DATEINCONNUE);

        // Test protection null
        objetTest.setDateMEP(null);
        assertThat(objetTest.getDateMEP()).isEqualTo(DATEINCONNUE);
    }

    @Test
    public void testGetTypeEdition(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test sans initialisation
        assertThat(objetTest.getTypeEdition()).isEqualTo(TypeEdition.INCONNUE);

        // Test setter et getter
        objetTest.setTypeEdition(TypeEdition.MAJEURE);
        assertThat(objetTest.getTypeEdition()).isEqualTo(TypeEdition.MAJEURE);

        // Test protection null
        objetTest.setTypeEdition(null);
        assertThat(objetTest.getTypeEdition()).isEqualTo(TypeEdition.MAJEURE);
    }

    @Test
    public void testGetEditionMajeure(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEditionMajeure(), s -> objetTest.setEditionMajeure(s));
    }

    @Test
    public void testIsActif(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isActif(), (b) -> objetTest.setActif(b));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

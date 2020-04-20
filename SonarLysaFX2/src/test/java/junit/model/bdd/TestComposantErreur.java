package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.ComposantErreur;
import model.enums.Param;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposantErreur extends TestAbstractBDDModele<ComposantErreur, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl()
    {
        objetTest.setKey(KEY);
        objetTest.setNom(NOM);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création objet avec constructeur parametre
        ComposantErreur compoErr = ComposantErreur.build(KEY, NOM);

        // Test des valeurs
        assertThat(compoErr.getKey()).isNotNull();
        assertThat(compoErr.getKey()).isEqualTo(KEY);
        assertThat(compoErr.getNom()).isNotNull();
        assertThat(compoErr.getNom()).isEqualTo(NOM);
        assertThat(compoErr.isaPurger()).isTrue();
        assertThat(compoErr.getDateDetection()).isNotNull();
        assertThat(compoErr.getDateDetection()).isEqualTo(LocalDate.now());

        // Test exception paramètre 1 null
        assertThrows(IllegalArgumentException.class, () -> ComposantErreur.build(null, NOM));

        // Test exception parmètre 1 vide
        assertThrows(IllegalArgumentException.class, () -> ComposantErreur.build(Statics.EMPTY, NOM));

        // Test exception parmètre 2 null
        assertThrows(IllegalArgumentException.class, () -> ComposantErreur.build(KEY, null));

        // Test exception parmètre 2 vide
        assertThrows(IllegalArgumentException.class, () -> ComposantErreur.build(KEY, Statics.EMPTY));
    }

    @Test
    public void testGetMapIndex(TestInfo testInfo)
    {
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getKey());
    }

    @Test
    public void testAjouterPurge(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test valeur initiale
        assertThat(objetTest.getNbrePurge()).isEqualTo(0);

        // Appel méthode
        objetTest.ajouterPurge();
        assertThat(objetTest.getNbrePurge()).isEqualTo(1);
    }

    @Test
    public void testEqualse(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        objetTest.setaPurger(true);
        objetTest.setDateDetection(today);
        objetTest.setExiste(true);
        objetTest.setKey(KEY);
        objetTest.setNbrePurge(123);
        objetTest.setNom(NOM);

        // Test simple
        ComposantErreur compo = ModelFactory.build(ComposantErreur.class);
        testSimpleEquals(compo);

        // Test autres paramètres
        compo.setaPurger(true);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setDateDetection(today);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setExiste(true);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setKey(KEY);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setNbrePurge(123);
        assertThat(objetTest.equals(compo)).isFalse();

        compo.setNom(NOM);
        assertThat(objetTest.equals(compo)).isTrue();
    }
    
    @Test
    public void testHashCode(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation objetTest
        objetTest.setaPurger(true);
        objetTest.setDateDetection(today);
        objetTest.setExiste(true);
        objetTest.setKey(KEY);
        objetTest.setNbrePurge(123);
        objetTest.setNom(NOM);

        ComposantErreur compo = ModelFactory.build(ComposantErreur.class);

        // Test 
        compo.setDateDetection(today);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setKey(KEY);
        assertThat(objetTest.hashCode()).isNotEqualTo(compo.hashCode());

        compo.setNom(NOM);
        assertThat(objetTest.hashCode()).isEqualTo(compo.hashCode());
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        String nom = "autreNom";
        objetTest.setNom(nom);
        assertThat(objetTest.getNom()).isEqualTo(nom);

        // Protection null et vide
        objetTest.setNom(null);
        assertThat(objetTest.getNom()).isEqualTo(nom);
        objetTest.setNom(Statics.EMPTY);
        assertThat(objetTest.getNom()).isEqualTo(nom);
    }

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getKey()).isEqualTo(KEY);
        String key = "autreKey";
        objetTest.setKey(key);
        assertThat(objetTest.getKey()).isEqualTo(key);

        // Protection null et vide
        objetTest.setKey(null);
        assertThat(objetTest.getKey()).isEqualTo(key);
        objetTest.setKey(Statics.EMPTY);
        assertThat(objetTest.getKey()).isEqualTo(key);
    }

    @Test
    public void testGetDateDetection(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateDetection(), d -> objetTest.setDateDetection(d));
    }

    @Test
    public void testGetLiens(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.getLiens()).isNotNull();
        assertThat(objetTest.getLiens()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS) + KEY);
    }

    @Test
    public void testIsaPurger(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isaPurger(), (b) -> objetTest.setaPurger(b));
    }

    @Test
    public void testGetNbrePurge(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getNbrePurge(), i -> objetTest.setNbrePurge(i));
    }

    @Test
    public void testExiste(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.existe(), s -> objetTest.setExiste(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

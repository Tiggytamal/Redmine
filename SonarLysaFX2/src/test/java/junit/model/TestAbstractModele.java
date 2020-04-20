package junit.model;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.JunitBase;
import model.AbstractModele;
import model.ModelFactory;
import model.bdd.AbstractBDDModele;
import utilities.TechnicalException;
import utilities.Utilities;

public abstract class TestAbstractModele<T extends AbstractModele> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @SuppressWarnings("unchecked")
    @Override
    public final void init() throws Exception
    {
        // Permet de récupérer la classe sous forme de type parametre
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // instanciation du paramètre, récupération de la classe, et création du handler depuis la factory
        try
        {
            objetTest = ModelFactory.build((Class<T>) Class.forName(parameterClassName));
        }
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("Impossible d'instancier l'énumération - junit.model.AbstractTestModel.init", e);
        }

        initImpl();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Implementation de l'initialisation si besoin pour les classes filles
     */
    protected void initImpl()
    {

    }

    /**
     * Test un couple getter / setter une {@link java.lang.Boolean} avec initialisation à faux
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleBoolean(TestInfo testInfo, Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(getter.get()).isFalse();
        setter.accept(true);
        assertThat(getter.get()).isTrue();
    }

    /**
     * Test un couple getter / setter d'un {@link java.lang.Integer} avec initialisation à 0
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleInteger(TestInfo testInfo, Supplier<Integer> getter, Consumer<Integer> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(getter.get().intValue()).isEqualTo(0);
        setter.accept(123);
        assertThat(getter.get().intValue()).isEqualTo(123);
    }

    /**
     * Test un couple getter / setter d'un {@link java.lang.String} avec protection par methode getString
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleString(TestInfo testInfo, Supplier<String> getter, Consumer<String> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec getString
        assertWithMessage("Retour getter null : ").that(getter.get()).isNotNull();
        assertThat(getter.get()).isEmpty();

        // Test getter et setter
        setter.accept(TESTSTRING);
        assertThat(getter.get()).isEqualTo(TESTSTRING);

        // Test protection setter null
        setter.accept(null);
        assertWithMessage("Retour getter null : ").that(getter.get()).isNotNull();
        assertThat(getter.get()).isEmpty();
    }

    /**
     * Test un couple getter / setter d'une {@link java.util.List} avec protection de la nullité à l'initialisation, au getter et au setter
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     * @param objet
     *                 objet à ajouter dans la liste de test
     * @param champ
     *                 nom du champ
     * 
     * @param          <L>
     *                 Classe des objets présents dans la liste.
     * @throws IllegalAccessException
     *                                Exception lancée par la Whitebox.
     */
    protected <L> void testSimpleList(TestInfo testInfo, Supplier<List<L>> getter, Consumer<List<L>> setter, L objet, String champ) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation non null
        assertThat(getter.get()).isNotNull();
        assertThat(getter.get()).isEmpty();

        // Test getter et setter
        List<L> liste = new ArrayList<>();
        liste.add(objet);
        setter.accept(liste);
        assertThat(getter.get()).isEqualTo(liste);

        // Protection setter null et vide
        setter.accept(null);
        assertThat(getter.get()).isEqualTo(liste);
        setter.accept(new ArrayList<>());
        assertThat(getter.get()).isEqualTo(liste);

        // Protection null avec introspection
        setField(champ, null);
        assertThat(getter.get()).isNotNull();
        assertThat(getter.get()).isEmpty();
    }

    /**
     * Test un couple getter / setter d'une {@link java.util.List} d'un objet FXML depuis une {@link javafx.beans.property.ListProperty} avec protection
     * de la nullité au setter
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleListFXML(TestInfo testInfo, Supplier<List<String>> getter, Consumer<List<String>> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(getter.get()).isNotNull();
        assertThat(getter.get()).isEmpty();
        setter.accept(Arrays.asList(LOT123456, TESTSTRING));
        assertThat(getter.get().get(0)).isEqualTo(LOT123456);
        assertThat(getter.get().get(1)).isEqualTo(TESTSTRING);

        // Protection setter null
        setter.accept(null);
        assertThat(getter.get().get(0)).isEqualTo(LOT123456);
        assertThat(getter.get().get(1)).isEqualTo(TESTSTRING);
    }

    /**
     * Test un couple getter / setter pour une {@link java.time.LocalDate} avec protection sur setter anti-null
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleLocalDate(TestInfo testInfo, Supplier<LocalDate> getter, Consumer<LocalDate> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec valeur à null
        assertThat(getter.get()).isNull();

        // Test getter et setter
        setter.accept(today);
        assertThat(getter.get()).isEqualTo(today);

        // Test protection setter null
        setter.accept(null);
        assertThat(getter.get()).isNotNull();
        assertThat(getter.get()).isEqualTo(today);
    }

    /**
     * Test un couple getter / setter pour une {@link java.time.LocalTime} avec protection sur setter anti-null
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleLocalTime(TestInfo testInfo, Supplier<LocalTime> getter, Consumer<LocalTime> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec valeur à null
        assertThat(getter.get()).isNull();

        // Test getter et setter
        setter.accept(time);
        assertThat(getter.get()).isEqualTo(time);

        // Test protection setter null
        setter.accept(null);
        assertThat(getter.get()).isNotNull();
        assertThat(getter.get()).isEqualTo(time);
    }

    /**
     * Test un couple getter / setter pour une {@link java.time.LocalDateTime} avec protection anti-null sur le setter
     * 
     * @param testInfo
     *                 testInfo de la méthode de test appelante.
     * @param getter
     *                 getter du champ.
     * @param setter
     *                 setter du champ.
     */
    protected void testSimpleLocalDateTime(TestInfo testInfo, Supplier<LocalDateTime> getter, Consumer<LocalDateTime> setter)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec valeur à null
        assertThat(getter.get()).isNull();

        // Test getter et setter
        setter.accept(todayTime);
        assertThat(getter.get()).isEqualTo(todayTime);

        // Test protection setter null
        setter.accept(null);
        assertThat(getter.get()).isNotNull();
        assertThat(getter.get()).isEqualTo(todayTime);
    }
    

    /**
     * Permet de tester la fonction equals d'un AbstractModeleBDD avec idBase = 12345
     * 
     * @param obj2
     *             objet à tester.
     * @throws IllegalAccessException
     *                                Exception remontée par le WHitebox.
     */
    @SuppressWarnings("unlikely-arg-type")
    protected void testSimpleEquals(T obj2) throws IllegalAccessException
    {
        // Test objet identique
        assertThat(objetTest.equals(objetTest)).isTrue();

        // Test objet null
        assertThat(objetTest.equals(null)).isFalse();

        // Test objet autre classe
        assertThat(objetTest.equals(EMPTY)).isFalse();

        // Test objet différent
        setField("idBase", 12345);
        assertThat(objetTest.equals(obj2)).isFalse();

        // Test égalité
        Whitebox.getField(AbstractBDDModele.class, "idBase").set(obj2, getField("idBase"));
        assertThat(objetTest.equals(obj2)).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

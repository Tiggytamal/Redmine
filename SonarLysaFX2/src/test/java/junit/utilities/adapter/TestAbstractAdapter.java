package junit.utilities.adapter;

import static com.google.common.truth.Truth.assertThat;

import java.lang.reflect.ParameterizedType;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import junit.JunitBase;
import utilities.TechnicalException;
import utilities.Utilities;
import utilities.adapter.AbstractXmlAdapter;

@SuppressWarnings("rawtypes")
public abstract class TestAbstractAdapter<T extends AbstractXmlAdapter> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /*---------- CONSTRUCTEURS ----------*/

    @SuppressWarnings("unchecked")
    @Override
    @BeforeEach
    public void init() throws Exception
    {
        // Permet de récupérer la classe sous forme de type parametre
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // instanciation du paramètre, récupération de la classe, et création du handler depuis la factory
        try
        {
            objetTest = (T) Class.forName(parameterClassName).newInstance();
        }
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("Impossible d'instancier l'énumération - junit.model.AbstractTestModel.init", e);
        }
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Méthode pour tester la fonction marshal d'un Adapter
     * 
     * @param testInfo
     *                 testInfo de la méthode appelante.
     * @throws Exception
     *                   Exception lancée par les méthode marshal.
     * 
     */
    public abstract void testMarshal(TestInfo testInfo) throws Exception;

    /**
     * Méthode pour tester la fonction unmarshal d'un Adapter
     * 
     * @param testInfo
     *                 testInfo de la méthode appelante.
     * @throws Exception
     *                   Exception lancée par les méthode unmarshal.
     */
    public abstract void testUnmarshal(TestInfo testInfo) throws Exception;

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Test simple d'une méthode marshal
     * 
     * @param testInfo
     *                 testInfo de la méthode appelante.
     * @param toGet
     *                 Objet récupéré lors du get.
     * @param toSet
     *                 Objet récupéré lors du set.
     * @throws Exception
     *                   Exceptions lancées par les méthodes marshal et unmarshal.
     */
    @SuppressWarnings("unchecked")
    public void testSimpleMarshal(TestInfo testInfo, Object toGet, Object toSet) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test protection null
        assertThat(objetTest.marshal(null)).isNull();

        // Test méthode
        assertThat(objetTest.marshal(toSet)).isEqualTo(toGet);
    }

    /**
     * Test simple d'une méthode unmarshal
     * 
     * @param testInfo
     *                 testInfo de la méthode appelante.
     * @param toGet
     *                 Objet récupéré lors du get
     * @param toSet
     *                 Objet récupéré lors du set.
     * @throws Exception
     *                   Exceptions lancées par les méthodes marshal et unmarshal.
     */
    @SuppressWarnings("unchecked")
    public void testSimpleUnmarshal(TestInfo testInfo, Object toGet, Object toSet) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test protection null
        assertThat(objetTest.unmarshal(null)).isNull();

        // Test méthode
        assertThat(objetTest.unmarshal(toGet)).isEqualTo(toSet);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

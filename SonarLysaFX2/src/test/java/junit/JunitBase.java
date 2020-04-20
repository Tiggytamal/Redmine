package junit;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import model.DataBaseXML;
import model.Info;
import model.parsing.ProprietesPersoXML;
import model.parsing.ProprietesXML;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de base de tous les tests Junit. Permet de récupérer les fichier de paramètres depuis les resources et de simuler une connexion utlisateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public abstract class JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    /* Constantes statiques pour les tests */

    /** logger plantages de l'application */
    protected static final Logger LOGGER = Utilities.getLogger("console-log");

    /** commentaire, balise @Disabled des Junit */
    protected static final String TESTMANUEL = "Test manuel - ne pas utiliser en automatique";
    protected static final String CSS = "file:/D:/git/SonarLysaFX2/SonarLysaFX2/target/classes/application.css";
    protected static final String NOM = "nom";
    protected static final String KEY = "key";
    protected static final String LOT123456 = "123456";
    protected static final String TESTSTRING = "test";
    protected static final String EMPTY = Statics.EMPTY;
    protected static final String SEPARATION = "**********************";
    protected final LocalDateTime todayTime = LocalDateTime.now();
    protected final LocalDate today = LocalDate.now();
    protected final LocalTime time = LocalTime.now();

    protected DataBaseXML databaseXML;
    protected T objetTest;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeAll
    public static void initAll(TestInfo testInfo)
    {
        afficherClasse(testInfo);
    }

    @BeforeEach
    public void initData()
    {
        // Instanciateur statique des constantes utilisees par toutes les classes de test
        // Mock des fichiers de paramètres depuis les ressources
        ProprietesXML proprietes;
        Info info;
        ProprietesPersoXML propsPerso;
        try
        {
            proprietes = (ProprietesXML) JAXBContext.newInstance(ProprietesXML.class).createUnmarshaller().unmarshal(new File(JunitBase.class.getResource("/proprietes.xml").getFile()));
            info = (Info) JAXBContext.newInstance(Info.class).createUnmarshaller().unmarshal(new File(JunitBase.class.getResource("/info.xml").getFile()));
            propsPerso = (ProprietesPersoXML) JAXBContext.newInstance(ProprietesPersoXML.class).createUnmarshaller().unmarshal(new File(JunitBase.class.getResource("/MATHON Gregoire.xml").getFile()));
        }
        catch (JAXBException e)
        {
            throw new TechnicalException("junit.JunitBase.constructor - impossible d'instancier les statiques", e);
        }

        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, info);
        Whitebox.setInternalState(Statics.class, propsPerso);
    }

    /*---------- METHODES ABSTRAITES ----------*/

    public abstract void init() throws Exception;

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Utilise l'introspection pour valoriser un champ d'un objetTest à la valeur donnee
     * 
     * @param nom
     *              nom du champ à valoriser
     * @param value
     *              valeur du champ desire
     * @throws IllegalAccessException
     *                                Exception lancée par les accés Whitebox.
     */
    protected void setField(String nom, Object value) throws IllegalAccessException
    {
        Whitebox.getField(objetTest.getClass(), nom).set(objetTest, value);
    }

    /**
     * Utilise l'introspection pour récupérer la valeur d'un champ non public d'un objetTest
     * 
     * @param nom
     *            Nom du champ dans la classe.
     * @return
     *         La valorisation du champ demandé de l'objetTest.
     * @throws IllegalAccessException
     *                                Exception lancée par les accés Whitebox.
     */
    protected Object getField(String nom) throws IllegalAccessException
    {
        return Whitebox.getField(objetTest.getClass(), nom).get(objetTest);
    }

    /**
     * Affiche les informations de la classe au début de chaque classe de test.
     * 
     * @param testInfo
     *                 testInfo de la méthode appelante.
     */
    protected static void afficherClasse(TestInfo testInfo)
    {
        LOGGER.debug(EMPTY);
        LOGGER.debug(testInfo.getDisplayName());
        LOGGER.debug(SEPARATION);
    }

    protected void initDataBase() throws JAXBException
    {
        databaseXML = (DataBaseXML) JAXBContext.newInstance(DataBaseXML.class).createUnmarshaller().unmarshal(new File(JunitBase.class.getResource("/database.xml").getFile()));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package junit;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import model.DataBaseXML;
import model.Info;
import model.ProprietesXML;
import utilities.Statics;

/**
 * Classe de base de tous les tests Junit. Permet de récupérer les fichier de paramètres depuis les resources et de simuler une connexion utlisateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public abstract class JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected static final ProprietesXML proprietes = new ControlXML().recupererXMLResources(ProprietesXML.class);
    protected static final Info info = new ControlXML().recupererXMLResources(Info.class);
    protected final LocalDate today = LocalDate.now();

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private DataBaseXML dataBase;

    /*---------- CONSTRUCTEURS ----------*/

    protected JunitBase()
    {
        // Mock des fichiers de paramètres depuis les ressources
        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, info);

        dataBase = new ControlXML().recupererXMLResources(DataBaseXML.class);
    }

    /*---------- METHODES ABSTRAITES ----------*/

    @Before
    public abstract void init() throws Exception;

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

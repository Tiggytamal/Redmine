package junit;

import java.io.File;
import java.time.LocalDate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.powermock.reflect.Whitebox;

import model.Info;
import model.ProprietesXML;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de base de tous les tests Junit. Permet de récupérer les fichier de paramètres depuis les resources et de simuler une connexion utlisateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public abstract class JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected final LocalDate today = LocalDate.now();

    /*---------- CONSTRUCTEURS ----------*/

    protected JunitBase()
    {
        // Mock des fichiers de paramètres depuis les ressources
        ProprietesXML proprietes;
        Info info;
        try
        {
            proprietes = (ProprietesXML) JAXBContext.newInstance(ProprietesXML.class).createUnmarshaller().unmarshal(new File(getClass().getResource("/proprietes.xml").getFile()));
            info = (Info) JAXBContext.newInstance(Info.class).createUnmarshaller().unmarshal(new File(getClass().getResource("/info.xml").getFile()));
        }
        catch (JAXBException e)
        {
            throw new TechnicalException("junit.JunitBase.constructor - impossible d'instancier les statiques");
        }

        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, info);
    }

    /*---------- METHODES ABSTRAITES ----------*/

    @Before
    public abstract void init() throws Exception;

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}

package control.word;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import utilities.AbstractToStringImpl;
import utilities.Statics;
import utilities.Utilities;

/**
 * Classe abstraite de contrôle des fichiers Word
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public abstract class AbstractControlWord extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    protected static final String DOCX = ".docx";
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /** Fichier Excel à modifier */
    protected File file;

    protected XWPFDocument document;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractControlWord(File file)
    {
        this.file = file;
        document = new XWPFDocument();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Permet de créer le fichier word.
     * 
     * @return
     *         vrai si la création s'est bbine effectuee.
     */
    protected abstract boolean creerFichier();

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Enregistre le fichier Word sur le disque, et retourne si celui-ci existe bien.
     * 
     * @return
     *         Vrai si le fichier est bien enregistré.
     */
    protected boolean write()
    {
        try (OutputStream out = Files.newOutputStream(file.toPath()))
        {
            document.write(out);
            return file.exists();
        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(Statics.EMPTY, e);
            return false;
        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}

package control.word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import utilities.AbstractToStringImpl;

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
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log"); 
    
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
     * Permet de créer le fichier word
     */
    protected abstract void creerFichier();
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * Enregistre le fichier Word sur le disque
     */
    protected void write()
    {        
        try (FileOutputStream out = new FileOutputStream(file))
        {
            document.write(out);
        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(e);            
        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}

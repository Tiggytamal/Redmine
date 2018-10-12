package model;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.interfaces.AbstractModele;
import model.interfaces.XML;
import utilities.Statics;

/**
 * Classe repr�sentant le fichier XML de sauvegarde des donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class FichiersXML extends AbstractModele implements XML
{
    /*---------- ATTRIBUTS ----------*/

    //Constantes statiques
    private static final String NOMFICHIER = "\\fichiers.xml";
    private static final String RESOURCE = "/fichiers.xml";

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public File getFile()
    {
        return new File(Statics.JARPATH + NOMFICHIER);
    }

    @Override
    public File getResource()
    {
        return new File(JunitBase.class.getResource(RESOURCE).getFile());
    }

    @Override
    public String controleDonnees()
    {
        return Statics.EMPTY;
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/
}

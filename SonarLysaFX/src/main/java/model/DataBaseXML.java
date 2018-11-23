package model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.bdd.ComposantSonar;
import model.bdd.DefautQualite;
import model.interfaces.AbstractModele;
import model.interfaces.XML;
import utilities.Statics;

/**
 * Représentation XML de la base de données pour les tests
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class DataBaseXML extends AbstractModele implements XML
{

    /*---------- ATTRIBUTS ----------*/
    
    // Constantes statiques
    private static final String NOMFICHIER = "\\database.xml";
    private static final String RESOURCE = "/database.xml";
    
    // Tables
    private Map<String, ComposantSonar> mapCompos;
    private Map<String, DefautQualite> mapDQ;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    DataBaseXML()
    {
        mapCompos = new HashMap<>();
        mapDQ = new HashMap<>();
    }
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
   
    @XmlElementWrapper
    @XmlElement(name = "mapCompos", required = false)
    public Map<String, ComposantSonar> getMapCompos()
    {
        return mapCompos;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapDQ", required = false)
    public Map<String, DefautQualite> getMapDefautQualite()
    {
        return mapDQ;
    }
}

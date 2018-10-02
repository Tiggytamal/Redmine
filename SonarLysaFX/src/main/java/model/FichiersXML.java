package model;

import static utilities.Statics.NL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.enums.TypeDonnee;
import model.utilities.AbstractModele;
import model.utilities.XML;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe représentant le fichier XML de sauvegarde des données
 * 
 * @author ETP8137 - Grégoire Mathon
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
    
    private Map<String, String> mapProjetsNpc;

    private boolean controleOK;

    /*---------- CONSTRUCTEURS ----------*/

    FichiersXML()
    {
        mapProjetsNpc = new HashMap<>();
        controleOK = true;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FichiersXML majMapDonnees(TypeDonnee typeFichier, Map map)
    {
        if (map == null)
            return this;
        
        switch (typeFichier)
        {                
            case GROUPE:
                mapProjetsNpc.clear();
                mapProjetsNpc.putAll(map);
                break;

            default:
                throw new TechnicalException("FichiersXML.majMapDonnees - Type de fichier non géré :" + typeFichier.toString(), null);
        }

        return this;
    }

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement fichiers Excel :").append(NL);
        controleOK = true;

        if (!controleOK)
            builder.append("Merci de recharger le(s) fichier(s) de paramétrage.").append(NL);

        return builder.append(NL).toString();
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/
    
    @XmlElementWrapper
    @XmlElement(name = "mapProjetsNpc", required = false)
    public Map<String, String> getMapProjetsNpc()
    {
        return mapProjetsNpc;
    }
}

package junit;

import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import model.FichiersXML;
import model.ProprietesXML;
import utilities.Statics;

/**
 * Classe de base de tous les tests Junit. Permet de récupérer les fichier de paramètres depuis les resources
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public abstract class JunitBase
{   
    protected static final ProprietesXML proprietes = new ControlXML().recupererXMLResources(ProprietesXML.class);
    protected static final FichiersXML fichiers = new ControlXML().recupererXMLResources(FichiersXML.class);
    
    protected JunitBase()
    {
        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, fichiers);
    }
}

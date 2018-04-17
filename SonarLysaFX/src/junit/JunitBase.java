package junit;

import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import model.FichiersXML;
import model.Info;
import model.ModelFactory;
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
    protected static final ProprietesXML proprietes = new ControlXML().recupererXMLResources(ProprietesXML.class);
    protected static final FichiersXML fichiers = new ControlXML().recupererXMLResources(FichiersXML.class);
    protected static final Info info = ModelFactory.getModel(Info.class);
    
    protected JunitBase()
    {
        // Mock des fichiers de paramètres depuis les ressources
        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, fichiers);
        
        // Mock des infos utilisateurs
        info.setPseudo("ETP8137");
        info.setMotDePasse("28H02m8901,;:!");
        Whitebox.setInternalState(Statics.class, info);
    }
}

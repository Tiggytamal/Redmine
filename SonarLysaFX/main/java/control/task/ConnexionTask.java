package control.task;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import javafx.concurrent.Task;
import utilities.Statics;

public class ConnexionTask extends Task<Boolean>
{
    /*---------- ATTRIBUTS ----------*/
    
    private ITeamRepository repo;
    private IProgressMonitor progressMonitor;
    private Map<String, IProjectArea> pareas;
    private ControlRTC control = ControlRTC.INSTANCE;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public ConnexionTask()
    {

    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return connexionRTC();
    }

    /*---------- METHODES PRIVEES ----------*/
    
    private Boolean connexionRTC()
    {
        repo.registerLoginHandler((ITeamRepository repository) -> new UsernameAndPasswordLoginInfo(Statics.info.getPseudo(), Statics.info.getMotDePasse()));
        try
        {
            // Un erreur de login renvoie une erreur fonctionnelle
            repo.login(progressMonitor);
            if (pareas.isEmpty())
                control.recupererTousLesProjets();
        } catch (TeamRepositoryException e)
        {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
        
    }
    /*---------- ACCESSEURS ----------*/
    
}

package junit;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.sonar.SonarAPI;
import control.task.PurgeSonarTask;
import model.enums.Param;
import model.sonarapi.Parametre;
import utilities.Statics;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{
    
    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException
    {
        // Classe de test
        String handler = "";;
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8901,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        
        List<String> listeVersion = new ArrayList<>();
        listeVersion.add("15");
        listeVersion.add("112");
        listeVersion.add("14");
        Collections.sort(listeVersion, (s1,s2) -> Integer.valueOf(s2).compareTo(Integer.valueOf(s1)));   
        System.out.println(listeVersion);
        
        // 1. Préparation du mock
        SonarAPI mock = Mockito.mock(SonarAPI.class);
        
        // init du webtarget
        WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
        Whitebox.getField(SonarAPI.class, "webTarget").set(mock, webTarget);
        
        // Vrai appel getComposant
        Mockito.when(mock.getComposants()).thenCallRealMethod();

        // Vrai appel webservice
        Parametre param = new Parametre("search", "composant ");
        Mockito.when(mock.appelWebserviceGET(Mockito.anyString(), Mockito.refEq(param))).thenCallRealMethod();
        
        // Remplacement api par le mock
        Whitebox.getField(PurgeSonarTask.class, "api").set(handler, mock);
       
    }
}
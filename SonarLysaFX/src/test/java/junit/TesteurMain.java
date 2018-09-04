package junit;

import java.util.Base64;
import java.util.List;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.sonar.SonarAPI;
import model.sonarapi.Event;
import model.sonarapi.Projet;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{

    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException
    {
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8903,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));

        SonarAPI api = SonarAPI.INSTANCE;

        List<Projet> liste = api.getComposants();
        int i = 0;
        int j = 0;

        for (Projet projet : liste)
        {
            List<Event> events = api.getEventsComposant(projet.getKey());
            j++;
            if (events.isEmpty())
            {
                System.out.println(projet.getKey());
                i++;
            }
            
            if (j % 100 == 0)
                System.out.println(j);
        }
        System.out.println(i);
    }
}

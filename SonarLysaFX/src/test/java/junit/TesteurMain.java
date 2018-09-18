package junit;

import java.util.Base64;
import java.util.List;

import com.ibm.team.repository.common.TeamRepositoryException;

import dao.DaoComposantSonar;
import model.sql.ComposantSonar;

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
        
        DaoComposantSonar dao = new DaoComposantSonar();
        List<ComposantSonar> liste = dao.readAll();
        liste.size();
    }
}

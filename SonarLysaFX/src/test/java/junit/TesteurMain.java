package junit;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{

    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException, IOException
    {
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8904,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));   
        
        LocalDate today = LocalDate.now();
        System.out.println(today);
        LocalDate debutMois = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(1).minusDays(1L);
        System.out.println(debutMois);
        LocalDate fin = LocalDate.of(today.getYear(), today.getMonth(), 1);
        System.out.println(fin);
        LocalDate debutTrim = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(3).minusDays(1L);
        System.out.println(debutTrim);
    }
}

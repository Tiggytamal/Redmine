package junit;

import java.util.Base64;

import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
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
        System.out.println("A".compareTo("B"));
        System.out.println("\0");
        System.out.println("\0".hashCode());
       
    }
}
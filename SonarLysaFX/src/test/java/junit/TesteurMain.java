package junit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * Calsse permetant de faire des tests directement avec une entr�e JAVA classique
 * @author ETP8137 - Gr�goire Mathon
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
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
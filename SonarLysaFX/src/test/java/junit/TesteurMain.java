package junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import com.ibm.team.repository.common.TeamRepositoryException;

import model.enums.ParamSpec;
import utilities.Statics;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{
    
    public static void main(String[] args) throws TeamRepositoryException
    {
        // Classe de test
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
       
    }
}
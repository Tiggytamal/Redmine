package junit;

import static utilities.Statics.fichiersXML;

import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;

import model.InfoClarity;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{
    
    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException
    {
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();
        System.out.println(map.size());
        System.out.println(map.containsKey("RE005701"));
        for (String string : map.keySet())
        {
            if (string.contains("RE00"))
                System.out.println(string);
        }
       
    }
}
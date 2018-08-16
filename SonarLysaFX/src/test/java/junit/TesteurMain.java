package junit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.xml.ControlXML;
import model.ComposantSonar;
import model.FichiersXML;
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
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8903,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));     
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        FichiersXML fichiersXML = new ControlXML().recupererXML(FichiersXML.class);
        Map<String, ComposantSonar> map =  fichiersXML.getMapComposSonar();
        int i = 0;
        for (ComposantSonar compo : map.values())
        {
            i += compo.getLdc();
        }
        
        System.out.println(i);
    }
}